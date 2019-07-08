import requests  
import json
import base64
import time
from datetime import datetime
import os
url = "http://insert your API URL here"

#################################################################
# Dummy data for the tray baseline that the algorithm output is
# compared against to determine whether a screw was used

trayParams = {
    1: {'nrows':10,'ncols':24}, #large
    2: {'nrows':8,'ncols':18}, #medium
    3:{'nrows':8,'ncols':16} #small
    }

alphabet = "ABCDEFGHIJ"
trayBaseline=[]
for region in trayParams:
    nrows=trayParams[region]['nrows']
    ncols=trayParams[region]['ncols']
    for r in range(0,nrows):
        rowalpha=alphabet[r]
        rownum=r+1
        for c in range(0,ncols):
            col=c+1
            # create a test pattern, fill in half of the baseline tray with screws
            if (rownum > nrows/2.0):
                trayBaseline.append({'HOLE_NUMBER':rowalpha+str(col),'TRAY_GROUP':region,'SCREW_ID':'1'})

######################################################
# Dummy data for testing override functionality

overrideTest = [
    {'HOLE_NUMBER':'A1','TRAY_GROUP':1,'SCREW_ID':1},
    {'HOLE_NUMBER':'A2','TRAY_GROUP':1,'SCREW_ID':0},
    {'HOLE_NUMBER':'B1','TRAY_GROUP':1,'SCREW_ID':0},
    {'HOLE_NUMBER':'B2','TRAY_GROUP':1,'SCREW_ID':1},
    {'HOLE_NUMBER':'I1','TRAY_GROUP':1,'SCREW_ID':1},
    {'HOLE_NUMBER':'I2','TRAY_GROUP':1,'SCREW_ID':0},
    {'HOLE_NUMBER':'J1','TRAY_GROUP':1,'SCREW_ID':0},
    {'HOLE_NUMBER':'J2','TRAY_GROUP':1,'SCREW_ID':1},
    {'HOLE_NUMBER':'A1','TRAY_GROUP':2,'SCREW_ID':1},
    {'HOLE_NUMBER':'A2','TRAY_GROUP':2,'SCREW_ID':0},
    {'HOLE_NUMBER':'B1','TRAY_GROUP':2,'SCREW_ID':0},
    {'HOLE_NUMBER':'B2','TRAY_GROUP':2,'SCREW_ID':1},
    {'HOLE_NUMBER':'G1','TRAY_GROUP':2,'SCREW_ID':1},
    {'HOLE_NUMBER':'G2','TRAY_GROUP':2,'SCREW_ID':0},
    {'HOLE_NUMBER':'H1','TRAY_GROUP':2,'SCREW_ID':0},
    {'HOLE_NUMBER':'H2','TRAY_GROUP':2,'SCREW_ID':1},
    {'HOLE_NUMBER':'A1','TRAY_GROUP':3,'SCREW_ID':1},
    {'HOLE_NUMBER':'A2','TRAY_GROUP':3,'SCREW_ID':0},
    {'HOLE_NUMBER':'B1','TRAY_GROUP':3,'SCREW_ID':0},
    {'HOLE_NUMBER':'B2','TRAY_GROUP':3,'SCREW_ID':1},
    {'HOLE_NUMBER':'G1','TRAY_GROUP':3,'SCREW_ID':1},
    {'HOLE_NUMBER':'G2','TRAY_GROUP':3,'SCREW_ID':0},
    {'HOLE_NUMBER':'H1','TRAY_GROUP':3,'SCREW_ID':0},
    {'HOLE_NUMBER':'H2','TRAY_GROUP':3,'SCREW_ID':1}
]

#########################################################
# Test one pic

pic_name = '1MP001.jpg'

#########################################################
# Test without override

with open(pic_name, 'rb') as f:
    img = base64.b64encode(f.read())
    img = img.decode('utf-8')

data = {'picture':img, 'trayBaseline': trayBaseline}
data=json.dumps(data)
print(pic_name)
r = requests.post(url, data=data).text
r = json.loads(r)
markedImage = r['markedImage'].encode('utf-8')
markedImage = base64.b64decode(markedImage)

usedScrews = json.loads(r['usedScrews'])
fullResult = json.loads(r['fullResult'])
statusFlag = r['statusFlag']
print('the status of flag is %d' % statusFlag)
with open('results/original/'+pic_name, 'wb') as f:
    f.write(markedImage)

##########################################################
# Test overriding algorithm results (when user makes corrections in the app)

data = {'picture':img, 'trayBaseline': trayBaseline, 'overrideHoles':overrideTest}
data=json.dumps(data)

r = requests.post(url, data=data).text
r = json.loads(r)
markedImage = r['markedImage'].encode('utf-8')
markedImage = base64.b64decode(markedImage)

usedScrews = json.loads(r['usedScrews'])
fullResult = json.loads(r['fullResult'])
statusFlag = r['statusFlag']
print('the status of flag is %d' % statusFlag)
with open('results/override/'+pic_name, 'wb') as f:
    f.write(markedImage)
