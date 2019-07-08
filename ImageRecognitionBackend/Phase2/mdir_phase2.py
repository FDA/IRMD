## pip3 install grpcio
from grpc.beta import implementations

## get  https://github.com/Vetal1977/tf_serving_example/tree/master/tensorflow_serving/apis
from tensorflow_serving.apis import predict_pb2
from tensorflow_serving.apis import prediction_service_pb2

import tensorflow as tf
import numpy as np
import cv2
import os
import pandas as pd
import json

classMapping = {
    'tray':1,
    'implantA1':2, 'slotA1':28,
    'implantA2':3, 'slotA2':29,
    'implantA3':4, 'slotA3':30,
    'implantA4':5, 'slotA4':31,
    'implantA5':6, 'slotA5':32,
    'implantB1':7, 'slotB1':33,
    'implantB2':8, 'slotB2':34,
    'implantB3':9, 'slotB3':35,
    'implantB4':10, 'slotB4':36,
    'implantB5':11, 'slotB5':37,
    'implantB6':12, 'slotB6':38,
    'implantB7':13, 'slotB7':39,
    'implantB8':14, 'slotB8':40,
    'implantC1':15, 'slotC1':41,
    'implantC2':16, 'slotC2':42,
    'implantC3':17, 'slotC3':43,
    'implantC4':18, 'slotC4':44,
    'implantC5':19, 'slotC5':45,
    'implantC6':20, 'slotC6':46,
                    'slotD1':47,
                    'slotD2':48,
    'implantD3':21, 'slotD3':49,
    'implantD4':22, 'slotD4':50,
    'implantE1':23, 'slotE1':51,
    'implantE2':24, 'slotE2':52,
    'implantE3':25, 'slotE3':53,
    'implantE4':26, 'slotE4':54,
    'implantE5':27, 'slotE5':55
}
classDecoding = {y:x for x,y in classMapping.items()}

colorDict={
    'Removed':(0,0,255), #red
    'Present':(0,255,0), #green
    'other': (0,255,255) #yellow
}

desiredW = 1000 #marked image
desiredH = 750 #marked image
desiredAspectRatio = desiredW/desiredH

def runObjectDetection(im_binary):
    host = '172.17.0.2' # docker container running locally
    port = 9000
    channel = implementations.insecure_channel(host, port)
    stub = prediction_service_pb2.beta_create_PredictionService_stub(channel)

    request = predict_pb2.PredictRequest()
    request.model_spec.name = 'mdir'
    request.model_spec.signature_name = 'serving_default'
    request.inputs['inputs'].CopyFrom(
    tf.contrib.util.make_tensor_proto(im_binary, shape=[1]))

    result = stub.Predict(request, 30.0)  # 60 secs timeout

    return result


trayLocations = ['A1','A2','A3','A4','A5','B1','B2','B3','B4','B5','B6','B7','B8',
             'C1','C2','C3','C4','C5','C6','D1','D2','D3','D4','E1','E2','E3','E4','E5']
def fillInUndetected(df,defaultGuess='implant'):
    missing = [x for x in trayLocations if x not in df['location'].unique()]
    idx=-1
    for item in missing:
        df = df.append(pd.DataFrame({'y1':1,'x1':1,'y2':2,'x2':2,'class':defaultGuess+item,'location':item,'score':-1},index=[idx]))
        idx = idx - 1
    return df

#convert Tensorflow Serving result to DataFrame, keeping all results
def convertResultToDF(result): 
    num_detections = int(result.outputs['num_detections'].float_val[0])
    boxvals = np.array(result.outputs['detection_boxes'].float_val[:num_detections*4]).reshape((-1,4))
    classes = [classDecoding[x] for x in result.outputs['detection_classes'].float_val][:num_detections]
    scores = result.outputs['detection_scores'].float_val[:num_detections]

    column_names = ['y1', 'x1', 'y2', 'x2']
    resultDF = pd.DataFrame(boxvals,columns=column_names)
    resultDF['x1'] = resultDF['x1']*desiredW
    resultDF['y1'] = resultDF['y1']*desiredH
    resultDF['x2'] = resultDF['x2']*desiredW
    resultDF['y2'] = resultDF['y2']*desiredH
    resultDF = resultDF.astype(int)
    resultDF['class'] = classes
    resultDF['location'] = resultDF['class'].apply(lambda s:s[-2:] if s != 'tray' else s)
    resultDF['score'] = scores
    #print('tray size',getTraySize(resultDF),'num detected',getNumDetected(resultDF))
    
    return resultDF

def getNumDetected(df):
    return len(df[df['class']!='tray']['location'].unique())

def getTraySize(df,minTrayScore=0.5):
    if len(df) == 0:
        return 0
    trayDF = df[df['class']=='tray']
    if len(trayDF)==0:
        return 0
    tray = trayDF.sort_values(by=['score'],ascending=False).iloc[0]
    if tray['score']<minTrayScore:
        return 0
    return abs((tray['x2']-tray['x1'])*(tray['y1']-tray['y2'])/desiredH/desiredW*100)

#filter to one result per location and check for errors
def filterResults(resultDF,minTrayArea=45,minDetections=26):
    traysize = getTraySize(resultDF)
    if traysize == 0:
        print('Error 1: No tray found. Retake picture.')
        return 1,None
    elif traysize < minTrayArea:
        print('Error 2: Tray is too small in the picture. Retake picture.')
        return 2,None
    filteredDF = resultDF[resultDF['class']!='tray']
    idx = filteredDF.groupby(['location'])['score'].transform(max) == filteredDF['score']
    filteredDF = filteredDF[idx].sort_values(by='location')
    if filteredDF.shape[0] < minDetections:
        print('Error 3: Not enough implants/slots were detected. Retake picture.')
        return 3,None
    elif filteredDF.shape[0] < 28:
        print('Warning: Not all 28 implants/slots were detected. Retake picture if needed. Found',filteredDF.shape[0])
        filteredDF = fillInUndetected(filteredDF)
    return 0,filteredDF

def processResult(df,baseline,override):

    baselineDF = pd.DataFrame(baseline).set_index('HOLE_NUMBER').drop('TRAY_GROUP',axis=1).astype(int)
    tmp = df.join(baselineDF,on='location').fillna(0)
    tmp['detect'] = tmp['class'].apply(lambda s: 1 if s[:-2] == 'implant' else 0)

    if override != None:
        for item in override:
            tmp.loc[tmp['location']==item['HOLE_NUMBER'],'detect'] = int(item['SCREW_ID'])
    tmp['SCREW_STATUS'] = ''
    tmp.loc[(tmp['detect']==1) & (tmp.SCREW_ID==1), 'SCREW_STATUS'] = 'Present'
    tmp.loc[(tmp['detect']==1) & (tmp.SCREW_ID==0), 'SCREW_STATUS'] = 'other'
    tmp.loc[(tmp['detect']==0) & (tmp.SCREW_ID==1), 'SCREW_STATUS'] = 'Removed'
    tmp.loc[(tmp['detect']==0) & (tmp.SCREW_ID==0), 'SCREW_STATUS'] = 'empty'
    tmp['TRAY_GROUP']=1
    tmp['HOLE_NUMBER']=tmp['location']
    return tmp[tmp['SCREW_STATUS']!='empty']

def markImage(img,df):
    for idx,row in df.iterrows():
        if row['SCREW_STATUS'] != '':
            cv2.rectangle(img, (row['x1'],row['y1']), (row['x2'],row['y2']),
                          color=colorDict[row['SCREW_STATUS']],
                          thickness=2, lineType=cv2.LINE_4)
    return img

def runAlgorithm_Tray2(im_binary, baseline=None,  override=None):
    #im = cv2.resize(img,(1000,750), interpolation=cv2.INTER_AREA)
    im = cv2.imdecode(np.fromstring(im_binary, np.uint8), cv2.IMREAD_COLOR)
    imH, imW = im.shape[0:2]
    if abs(imW/imH - desiredAspectRatio) > 0.01:
        print('warning: image aspect ratio not 4:3')
    if (imH != desiredH) and (imW !=desiredW):
        im =  cv2.resize(im,(desiredW,desiredH), interpolation=cv2.INTER_AREA)

    result = runObjectDetection(im_binary)
    resultDF = convertResultToDF(result)
    flag,filteredDF = filterResults(resultDF)
    if flag != 0:
        return flag,None,None,None
    fullResultsDF = processResult(filteredDF,baseline,override)
    markedImage = markImage(im,fullResultsDF)
    fullResults = fullResultsDF[['HOLE_NUMBER','SCREW_STATUS','TRAY_GROUP']].to_dict(orient='records')
    return 0,markedImage, json.dumps({}), json.dumps(fullResults)
