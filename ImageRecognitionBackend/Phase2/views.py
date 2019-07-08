
from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from django.http import StreamingHttpResponse, JsonResponse, HttpResponse

import json
import cv2
import base64
import time
from tray2 import mdir_phase2
from datetime import datetime
import os
import numpy as np


def getResult(img, trayBaseline, overrideHoles = None):
    statusFlag, markedImage, usedScrews, fullResult = mdir_phase2.runAlgorithm_Tray2(img,baseline=trayBaseline, override = overrideHoles)	
    return statusFlag, markedImage, usedScrews, fullResult

def save(name, data):
    # save the json
    with open(name+'.json','w') as f:
        f.write(json.dumps(data))

#--------------------------------------------change func-----------------
@csrf_exempt
def runMDIR_tray2(request):
    if request.method=='POST':

        ############################################################### receive the json data
        recvJson=json.loads(request.body.decode("utf-8"))
        recvImg = recvJson['picture'].encode('utf-8')
        recvImg = base64.b64decode(recvImg)
        trayBaseline = recvJson['trayBaseline']


        try:
            overrideHoles = recvJson['overrideHoles']
            save('receive', trayBaseline+overrideHoles)
        except:
            overrideHoles = -1 # -1 represent there is no overrideHoles
            save('receive', trayBaseline)
        if overrideHoles == -1:
            statusFlag, markedImage, usedScrews, fullResult = getResult(recvImg, trayBaseline)
        else:
            statusFlag, markedImage, usedScrews, fullResult = getResult(recvImg, trayBaseline, overrideHoles)


        ############################################################## prepare response
        if statusFlag == 0:

            img = cv2.imencode('.jpg', markedImage)[1].tostring()
            img = base64.b64encode(img)
            img = img.decode('utf-8')
        else:
            img = None

        # prepare the json data and respond

        sendData = {'statusFlag':statusFlag, 'markedImage':img, 'usedScrews':usedScrews, 'fullResult':fullResult}
        sendJson = json.dumps(sendData)

        try:
            # save the json
            with open('response.json','w') as f:
                f.write(json.dumps(usedScrews + fullResult))
        except:
            pass

        return HttpResponse(sendJson, content_type="application/json")

    return HttpResponse('it was GET request for tray2')

