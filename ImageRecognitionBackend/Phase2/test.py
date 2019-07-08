import requests  
import json
import base64
import time
from datetime import datetime
import os

#url = "http://ec2-.compute-1.amazonaws.com:80/tray2"
url = "http://insert API URL here"

pic_name = 'IMG_7429.JPG'
with open('test_source/'+pic_name, 'rb') as f:
    img = base64.b64encode(f.read())
    img = img.decode('utf-8')

trayBaseline=[]

group = {'A':5,'B':8,'C':6,'D':4,'E':5}

for k, v in group.items():
    for i in range(1,v+1):
        if i <= v // 2:
            trayBaseline.append({'HOLE_NUMBER':k+str(i),'TRAY_GROUP':ord(k) - 64,'SCREW_ID':'0'})
        else:
            trayBaseline.append({'HOLE_NUMBER':k+str(i),'TRAY_GROUP':ord(k) - 64,'SCREW_ID':'1'})

data = {'picture': img, 'trayBaseline':trayBaseline}
data=json.dumps(data)

#--------------------------------
with open('test_source/'+pic_name.split('.')[0]+'.json', 'w') as f:
        f.write(data)
#---------------------------------
r = requests.post(url, data=data).text

r = json.loads(r)
print("ok-------------------------------------------------------------------------------")
markedImage = r['markedImage'].encode('utf-8')
markedImage = base64.b64decode(markedImage)
dummy = json.dumps(r)

statusFlag = r['statusFlag']
print('the status of flag is %d' % statusFlag)
#-----------------------------------
with open('test_results/'+pic_name.split('.')[0]+'.jpg', 'wb') as f:
        f.write(markedImage)
with open('test_results/'+pic_name.split('.')[0]+'.json', 'w') as f:
        f.write(dummy)
#--------------------------------------




