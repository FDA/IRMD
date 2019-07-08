##########################
# input parameters in a json file:
#{
#	'picture':img, 
#	'trayBaseline': trayBaseline, 
#	'overrideHoles':overrideHoles
#}
# img is an image encoded by base64 and decoded by utf-8
# trayBaseline is json format
# overrideHoles is json format, optional

# outputparameters in a json file:
#{
#	'statusFlag':statusFlag, 
#	'markedImage':img, 
#	'usedScrews':usedScrews, 
#	'fullResult':fullResult
#}
# statusFlag (int): 0 means success, 1 means failure at edge detection, 2 means failure in grid detection
# img is an image encoded by base64 and decoded by utf-8, if statusFlag is not 0, None
# usedScrews is json format, if statusFlag is not 0, None
# fullResult is json format, if statusFlag is not 0, None
##########################




from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from django.http import StreamingHttpResponse, JsonResponse, HttpResponse

import json
from app import mdir
import cv2
import base64
import time
from datetime import datetime
import os
import numpy as np

imagePath = '/home/ec2-user/server/data/images/'
resultPath = '/home/ec2-user/server/data/results/'
def checkPath(Path):
    if not os.path.isdir(Path):
        os.mkdir(Path)

def nameImg():
    now = time.time()
    fileName = datetime.fromtimestamp(now).strftime('%Y-%m-%d-%H-%M-%S')
    return fileName

def saveRecvImg(img, fileName, rawImage = True):
    if rawImage:
        Path = imagePath
        with open(Path + fileName + '.jpg','wb') as f:
            f.write(img)
    else:
        Path = resultPath
        cv2.imwrite(Path + fileName + '.jpg',img)

def crop_pic(img):
    row_sum = np.sum(img, axis = 1)
    not_black_index = np.unique(np.where(row_sum != 0)[0])
    return img[not_black_index]

def getResult(img, trayBaseline, overrideHoles = None):
    statusFlag, markedImage, usedScrews, fullResult = mdir.runAlgorithm(img,baseline=trayBaseline, overrideHoles = overrideHoles)
    return statusFlag, markedImage, usedScrews, fullResult

checkPath(imagePath)
checkPath(resultPath)

@csrf_exempt
def runMDIR(request):
    if request.method=='POST':
        ############################################################### receive the json data
        recvJson=json.loads(request.body.decode("utf-8"))
        recvImg = recvJson['picture'].encode('utf-8')
        trayBaseline = recvJson['trayBaseline']
        try:
            overrideHoles = recvJson['overrideHoles']
        except:
            overrideHoles = -1 # -1 represent there is no overrideHoles

        # save the image to images directory
        fileName = nameImg()
        recvImg = base64.b64decode(recvImg)
        saveRecvImg(recvImg, fileName, rawImage = True)

        # save the recv json
        with open(imagePath + fileName + '.json','w') as f:
            f.write(request.body.decode("utf-8"))

        ############################################################## run mdir algorithm
        img = cv2.imread(imagePath + fileName + '.jpg')
        img = crop_pic(img)

        if overrideHoles == -1:
            statusFlag, markedImage, usedScrews, fullResult = getResult(img, trayBaseline)
        else:
            statusFlag, markedImage, usedScrews, fullResult = getResult(img, trayBaseline, overrideHoles)

        ############################################################## prepare response
        if statusFlag == 0:
            # save the marked image to results directory
            saveRecvImg(markedImage, fileName, rawImage = False)

            # read the marked image and decode by utf-8 to put into json
            with open(resultPath + fileName + '.jpg', 'rb') as f:
                img = base64.b64encode(f.read())
                img = img.decode('utf-8')
        else:
            img = None
        # prepare the json data and respond
        sendData = {'statusFlag':statusFlag, 'markedImage':img, 'usedScrews':usedScrews, 'fullResult':fullResult}
        sendJson = json.dumps(sendData)

        # save the total result
        with open(resultPath + fileName + '.json','w') as f:
            f.write(sendJson)

        return HttpResponse(sendJson, content_type="application/json")

    return HttpResponse('it was GET request')







