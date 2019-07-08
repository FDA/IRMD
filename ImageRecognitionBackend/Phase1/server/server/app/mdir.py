#This contains the methods for the image recognition algorithm
from scipy.ndimage.filters import gaussian_filter1d
from scipy.signal import argrelextrema
from math import sqrt
import re
from sklearn.externals import joblib
from sklearn.cluster import KMeans
from datetime import datetime
import numpy as np
import cv2
from matplotlib import pyplot as plt
plt.switch_backend('agg')
import os, errno
import json

figsizeLarge = (15,15)
figsizeSmall = (7,7)

def myImShow(im,sizeOption,hideTicks=False):
	if sizeOption=='S':
		fig = plt.figure(figsize=figsizeSmall)
	elif sizeOption=='L':
		fig = plt.figure(figsize=figsizeLarge)

	ax = fig.add_subplot(1,1,1) #1x1 grid, 1st subplot
	if hideTicks:
		ax.set_xticks([])
		ax.set_yticks([])
	
	if len(im.shape)==3:
		ax.imshow(bgr2rgb(im))
	elif len(im.shape)==2:
		ax.imshow(im,cmap='gray')
	plt.show()
		
def bgr2rgb(img): #OpenCV reads in images in BGR, need to convert to RGB if displaying with matplotlib
	return img[:,:,::-1]

def createDirectory(imgName=None): #create a directory to hold all algorithm outputs for a specific image
	imgName = datetime.now().strftime('%Y%m%d_%H%M%S_%f')[:-3] if imgName is None else imgName
	try:
		os.makedirs(imgName)
	except OSError as e:
		if e.errno != errno.EEXIST:
			raise
	return imgName

def find_tray(img,speedup):
	
	#helper functions
	def calcHorizontal(x1,y1,x2,y2): #y = mx+b (calculate y at x=desiredW/2)
		m=(y2-y1)/(x2-x1)
		b=y2-(m*x2)
		return m*desiredW/2+b

	def calcVertical(x1,y1,x2,y2): #x = my + b (calculate x at y=desiredH/2)
		m=(x2-x1)/(y2-y1)
		b=x2-(m*y2)
		return m*desiredH/2+b
	
	def line_intersection(line1, line2):
		xdiff = (line1[0][0] - line1[1][0], line2[0][0] - line2[1][0])
		ydiff = (line1[0][1] - line1[1][1], line2[0][1] - line2[1][1]) 

		def det(a, b):
			return a[0] * b[1] - a[1] * b[0]

		div = det(xdiff, ydiff)
		if div == 0:
			raise Exception('lines do not intersect')

		d = (det(*line1), det(*line2))
		x = det(d, xdiff) // div
		y = det(d, ydiff) // div
		return (x,y)
	
	# load the query image and convert to grayscale
	unscaledCopy = np.copy(img)
	if len(img.shape)==3:
		unscaledGray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
	else:
		unscaledGray = np.copy(img)
	imgH, imgW = unscaledGray.shape
	#if (imgW*1.0/imgH) < 1.32 or (imgW*1.0/imgH) > 1.34: #should be 4:3 ratio
	#	return 1,(0,0),(0,0),(0,0),(0,0)

	#aspectRatio = 4/3.0
	aspectRatio = img.shape[1]/img.shape[0] #landscape: width/height
	#print('aspectRatio',aspectRatio)
	
	#scale down to 2MP (1600x1200), remember scaling
	desiredW = 1600 #always set long edge to 1600
	desiredH = int(desiredW/aspectRatio)
	#print('desired H/W,',desiredW,desiredH)
	buffer = 10 #number of pixels considered to be the edge of the image
	
	if imgH > desiredH and imgW > desiredW:
		scaleFactor = imgW*1.0/desiredW
	elif imgH < desiredH and imgW < desiredW:
		scaleFactor = imgW*1.0/desiredW
	else:
		scaleFactor = 1
	#print('scaleFactor', scaleFactor)
	

	if speedup:
		kmeansScaledown = 5
	else:
		kmeansScaledown = 1
	if scaleFactor >= 1:
		houghInput = cv2.resize(unscaledGray,(desiredW,desiredH), interpolation=cv2.INTER_AREA)
		scaledImg = cv2.resize(img,(desiredW//kmeansScaledown,desiredH//kmeansScaledown), interpolation=cv2.INTER_AREA)
	elif scaleFactor < 1:
		houghInput = cv2.resize(unscaledGray,(desiredW,desiredH), interpolation=cv2.INTER_LINEAR)
		scaledImg = cv2.resize(img,(desiredW//kmeansScaledown,desiredH//kmeansScaledown), interpolation=cv2.INTER_LINEAR)
	houghInput = houghInput[buffer:desiredH-buffer,buffer:desiredW-buffer]
	scaledImg = scaledImg[buffer//kmeansScaledown:desiredH-buffer//kmeansScaledown,buffer//kmeansScaledown:desiredW-buffer//kmeansScaledown]
	
	def houghInput_HSV(img,speedup):
		shape= cv2.MORPH_ELLIPSE
		ksize= (3,3)
		kernel = cv2.getStructuringElement(shape,ksize)
		
		im = cv2.cvtColor(img,cv2.COLOR_BGR2HSV)
		#im = cv2.GaussianBlur(im,ksize=(11,11),sigmaX=3,sigmaY=3)
		if speedup:
			ks=(3,3)
		else:
			ks=(11,11)
		im = cv2.GaussianBlur(im,ksize=ks,sigmaX=3,sigmaY=3)
		im = im.reshape((-1,3)).astype(np.float64)
		#freq = cv2.getTickFrequency()
		#t1 = cv2.getTickCount()
		km = KMeans(n_clusters=2, n_init=5, max_iter=5, tol=1, random_state=2017).fit(im)
		#t2 = cv2.getTickCount()
		#print("Kmeans:",(t2-t1)/freq)
		res = km.labels_.reshape((img.shape)[0:2])
		res = np.uint8(res)*255
		#myImShow(res,'S')
		#colorRes = km.cluster_centers_[km.labels_.flatten()].reshape((img.shape))
		#myImShow(colorRes,'S')
		res = cv2.resize(res,(desiredW-2*buffer,desiredH-2*buffer),interpolation=cv2.INTER_LINEAR)
	
		#try different preprocessing steps for horizontal and vertical lines
		#verticalHoughInput = np.copy(res)
		verticalHoughInput = cv2.adaptiveThreshold(res,255,cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY,17,2)
		verticalHoughInput = 255- cv2.erode(verticalHoughInput,kernel) #make edges more visible using erosion
		verticalHoughInput[desiredH//4:desiredH*3//4,desiredW//5:desiredW*4//5]=0

		#horizontalHoughInput = np.copy(res)
		#horizontalHoughInput = cv2.adaptiveThreshold(horizontalHoughInput,255,cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY,17,2)
		#horizontalHoughInput = 255- cv2.erode(horizontalHoughInput,kernel) #make edges more visible using erosion
		#horizontalHoughInput[desiredH//4:desiredH*3//4,desiredW//5:desiredW*4//5]=0
		#horizontalHoughInput = np.copy(verticalHoughInput)
		horizontalHoughInput = verticalHoughInput
		
		return verticalHoughInput, horizontalHoughInput
		
	def houghInput_RGB(img):
		shape= cv2.MORPH_ELLIPSE
		ksize= (4,4)
		kernel = cv2.getStructuringElement(shape,ksize)
	
		#try different preprocessing steps for horizontal and vertical lines
		verticalHoughInput = np.copy(img)
		verticalHoughInput = cv2.GaussianBlur(verticalHoughInput,ksize=(11,11),sigmaX=3,sigmaY=9)
		verticalHoughInput = cv2.adaptiveThreshold(verticalHoughInput,255,cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY,17,2)
		verticalHoughInput = 255- cv2.erode(verticalHoughInput,kernel) #make edges more visible using erosion
		verticalHoughInput = cv2.medianBlur(verticalHoughInput,11)
		verticalHoughInput[desiredH//4:desiredH*3//4,desiredW//5:desiredW*4//5]=0
		
		horizontalHoughInput = np.copy(img)
		horizontalHoughInput = cv2.GaussianBlur(houghInput,ksize=(11,11),sigmaX=9,sigmaY=3)
		horizontalHoughInput = cv2.adaptiveThreshold(horizontalHoughInput,255,cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY,17,2)
		horizontalHoughInput = 255- cv2.erode(horizontalHoughInput,kernel) #make edges more visible using erosion
		horizontalHoughInput = cv2.medianBlur(horizontalHoughInput,11)
		horizontalHoughInput[desiredH//4:desiredH*3//4,desiredW//5:desiredW*4//5]=0
		
		return verticalHoughInput, horizontalHoughInput
	
	def houghLines(v_in, h_in, img): #scaledImg
		minHorizontalLineLength=640 #700 too high, 640 too high?
		minVerticalLineLength=480 #600 too high, 500 too high?
		horizontalLines= cv2.HoughLinesP(image=h_in,
										 rho=1,theta=np.pi/360, threshold=180,
										 lines=np.array([]), minLineLength=minHorizontalLineLength,maxLineGap=100)
		verticalLines = cv2.HoughLinesP(image=v_in,
										rho=2,theta=np.pi/360, threshold=120, #thresh was 120
										lines=np.array([]), minLineLength=minVerticalLineLength,maxLineGap=100)
		horizontalHoughResult=np.copy(img)
		verticalHoughResult=np.copy(img)
	
		if horizontalLines is None or verticalLines is None or len(horizontalLines)<2 or len(verticalLines)<2: #did not find any hough lines
			return (-1,-1),(-1,-1),(-1,-1),(-1,-1)
	
		#set initial (x1,y1,x2,y2) to find best bounding lines
		topLine = (0,desiredH,desiredW,desiredH) #as bottom as possible
		topLineScore = calcHorizontal(*topLine)
		bottomLine = (0,0,desiredW,0) #as top as possible
		bottomLineScore = calcHorizontal(*bottomLine)
		leftLine = (desiredW,0,desiredW,desiredH) #as right as possible
		leftLineScore = calcVertical(*leftLine)
		rightLine = (0,0,0,desiredH) #as left as possible
		rightLineScore = calcVertical(*rightLine)

		for houghLine in horizontalLines:
			x1,y1,x2,y2 = (houghLine[0][0], houghLine[0][1], houghLine[0][2], houghLine[0][3])
			angle = 90 if x2==x1 else np.abs(np.arctan((y2-y1)/(x2-x1)) * 180 / np.pi)
			if angle<=10: #horizontal line
				lineScore = calcHorizontal(x1,y1,x2,y2)
				if lineScore > bottomLineScore:
					bottomLine = (x1,y1,x2,y2)
					bottomLineScore = lineScore
				elif lineScore < topLineScore:
					topLine = (x1,y1,x2,y2)
					topLineScore = lineScore
			cv2.line(horizontalHoughResult, (x1,y1), (x2,y2), (0, 0, 255), 3, cv2.LINE_AA)
	
		for houghLine in verticalLines:
			x1,y1,x2,y2 = (houghLine[0][0], houghLine[0][1], houghLine[0][2], houghLine[0][3])
			angle = 90 if x2==x1 else np.abs(np.arctan((y2-y1)/(x2-x1)) * 180 / np.pi)
			if angle >= 80: #vertical line
				lineScore = calcVertical(x1,y1,x2,y2)
				if lineScore < leftLineScore:
					leftLine = (x1,y1,x2,y2)
					leftLineScore = lineScore
				elif lineScore > rightLineScore:
					rightLine = (x1,y1,x2,y2)
					rightLineScore = lineScore
			cv2.line(verticalHoughResult, (x1,y1), (x2,y2), (0, 0, 255), 3, cv2.LINE_AA)

		#myImShow(horizontalHoughResult,'S')
		#myImShow(verticalHoughResult,'S')

		tLine = np.array(topLine).reshape((2,2))
		bLine = np.array(bottomLine).reshape((2,2))
		rLine = np.array(rightLine).reshape((2,2))
		lLine = np.array(leftLine).reshape((2,2))

		u_left = line_intersection(tLine, lLine)
		u_right = line_intersection(tLine, rLine)
		b_right = line_intersection(bLine, rLine)
		b_left = line_intersection(bLine, lLine)

		linewidth=5
		scaledResult = np.copy(scaledImg)
		cv2.line(scaledResult, u_left, u_right, (0, 0, 255), linewidth, cv2.LINE_4)
		cv2.line(scaledResult, u_right, b_right, (0, 0, 255), linewidth, cv2.LINE_4)
		cv2.line(scaledResult, b_right, b_left, (0, 0, 255), linewidth, cv2.LINE_4)
		cv2.line(scaledResult, b_left, u_left, (0, 0, 255), linewidth, cv2.LINE_4)
		#myImShow(scaledResult,'S')

		u_left = tuple([int((val+buffer)*scaleFactor) for val in u_left])
		u_right = tuple([int((val+buffer)*scaleFactor) for val in u_right])
		b_right = tuple([int((val+buffer)*scaleFactor) for val in b_right])
		b_left = tuple([int((val+buffer)*scaleFactor) for val in b_left])
	
		unscaledResult = np.copy(img)
		cv2.line(unscaledResult, u_left, u_right, (0, 0, 255), linewidth, cv2.LINE_4)
		cv2.line(unscaledResult, u_right, b_right, (0, 0, 255), linewidth, cv2.LINE_4)
		cv2.line(unscaledResult, b_right, b_left, (0, 0, 255), linewidth, cv2.LINE_4)
		cv2.line(unscaledResult, b_left, u_left, (0, 0, 255), linewidth, cv2.LINE_4)
		#myImShow(unscaledResult,'S')
		
		return u_left, u_right, b_right, b_left
	
	def findDist(pt1,pt2):
		return sqrt((pt2[1]-pt1[1])**2 + (pt2[0]-pt1[0])**2)
	
	def isValidCorners(u_left,u_right,b_right,b_left):
	
		m_left = ((u_left[0]+b_left[0])/2,(u_left[1]+b_left[1])/2)
		m_right = ((u_right[0]+b_right[0])/2,(u_right[1]+b_right[1])/2)
		m_top = ((u_left[0]+u_right[0])/2,(u_left[1]+u_right[1])/2)
		m_bottom = ((b_left[0]+b_right[0])/2,(b_left[1]+b_right[1])/2)
		width = findDist(m_left,m_right)
		length = findDist(m_top,m_bottom)
		if (width < 500) or (length < 300):
			return False
		widthLengthRatio = width/length
		diagDistanceRatio = findDist(u_right,b_left)/findDist(u_left,b_right)
		diagDistanceRatio = 1.0/diagDistanceRatio if diagDistanceRatio>1 else diagDistanceRatio
		
		if  (widthLengthRatio < 1.41 or widthLengthRatio > 1.49) or (diagDistanceRatio < 0.984):
			#print('w/l ratio',widthLengthRatio)
			#print('diag ratio',diagDistanceRatio)
			return False
		return True
	
	#try method 1
	verticalHoughInput,horizontalHoughInput = houghInput_HSV(scaledImg,speedup)
	ul,ur,br,bl = houghLines(verticalHoughInput, horizontalHoughInput, scaledImg)
	if isValidCorners(ul,ur,br,bl):
		return 0,ul,ur,br,bl
	
	#try method 2
	verticalHoughInput,horizontalHoughInput = houghInput_RGB(houghInput)
	ul,ur,br,bl = houghLines(verticalHoughInput, horizontalHoughInput, scaledImg)
	if isValidCorners(ul,ur,br,bl):
		return 0,ul,ur,br,bl

	#print('Failed',imgName)
	return 1,(-1,-1),(-1,-1),(-1,-1),(-1,-1)

def get_local_normalize(img, Kernel_size = 3):
	blur_img = cv2.GaussianBlur(img, (Kernel_size, Kernel_size),0)
	diff_img = (img - blur_img)
	diff_img = cv2.absdiff(img,blur_img)
	return diff_img

def normalize(img):
	ratio = 255 / (np.mean(img)*10)
	return img * int(ratio)

def round2Odd(x): #round to next largest odd number
	return int(np.ceil(x)//2*2+1)

def findCircles(img,method,diameter,showParams=False,
				 nrow=None,ncol=None,
				 blurK=None,morphGradientK=None,
				 minDist=None,minRadius=None,maxRadius=None,
				 param1=None,param2=None):
	resultImg = img.copy()
	houghParams={}
	if method=='normalized':
		houghParams['minDist'] = int(diameter*1.05) if minDist is None else minDist
		houghParams['minRadius'] = int(diameter*0.8/2) if minRadius is None else minRadius
		houghParams['maxRadius'] = int(diameter*1.1/2) if maxRadius is None else maxRadius
		houghParams['param1'] = 200 if param1 is None else param1
		houghParams['param2'] = 10 if param2 is None else param2
		
		blurK = round2Odd(diameter/10.0) if blurK is None else blurK
		if showParams:
			print ('blurK',blurK)
		diff_img = get_local_normalize(img,Kernel_size=blurK)
		normal_img = normalize(diff_img)
		houghInput = cv2.cvtColor(normal_img, cv2.COLOR_BGR2GRAY)
	elif method=='unnormalized':
		houghParams['minDist'] = int(diameter*1.05) if minDist is None else minDist
		houghParams['minRadius'] = int(diameter*0.8/2) if minRadius is None else minRadius
		houghParams['maxRadius'] = int(diameter*1.1/2) if maxRadius is None else maxRadius
		houghParams['param1'] = 200 if param1 is None else param1
		houghParams['param2'] = 10 if param2 is None else param2
		
		houghInput = cv2.cvtColor(resultImg,cv2.COLOR_BGR2GRAY)
	elif method=='morphGradient':
		houghParams['minDist'] = int(diameter*1.05) if minDist is None else minDist
		houghParams['minRadius'] = int(diameter*0.8/2) if minRadius is None else minRadius
		houghParams['maxRadius'] = int(diameter*1.1/2) if maxRadius is None else maxRadius
		houghParams['param1'] = 35 if param1 is None else param1
		houghParams['param2'] = 10 if param2 is None else param2
		
		blurK = round2Odd(diameter/1.85) if blurK is None else blurK #previously 1.9
		#print blurK
		morphGradientK = round2Odd(diameter/5.0) if morphGradientK is None else morphGradientK #divide by 5.0 previously
		#print morphGradientK
		if showParams:
			print ('blurK',blurK)
			print ('morphologyGradientK', morphGradientK)
		
		blurred = cv2.medianBlur(cv2.cvtColor(resultImg,cv2.COLOR_BGR2GRAY),blurK)
		gradient_kernel = cv2.getStructuringElement(cv2.MORPH_ELLIPSE,(morphGradientK,morphGradientK))
		houghInput = 255 - cv2.morphologyEx(blurred,cv2.MORPH_GRADIENT,gradient_kernel,iterations=1)
	
	if showParams:
		for key in sorted(houghParams.keys()):
			print (key,houghParams[key])
		
	circlesFound = cv2.HoughCircles(houghInput,cv2.HOUGH_GRADIENT,dp=1,**houghParams)
	
	if circlesFound is None:
		#print 'No circles found'
		return resultImg,houghInput, []
	
	for circle in circlesFound[0,:]:
		cv2.circle(resultImg, (circle[0], circle[1]), circle[2], (0,0,255),3)
	#print 'Circles found',len(circlesFound[0])
	return resultImg,houghInput,circlesFound[0]
	

def plotMeans(img,imgName):
	rowMeans = []
	for row in img:
		rowMeans.append(np.mean(row))
	colMeans = []
	for col in img.T:
		colMeans.append(np.mean(col))

	fig = plt.figure(figsize=figsizeSmall)
	ax = fig.add_subplot(2,1,1) #2x1 grid, 1st subplot
	ax.plot(rowMeans)

	fig = plt.figure(figsize=figsizeSmall)
	ax = fig.add_subplot(2,1,2) #2x1 grid, 1st subplot
	ax.plot(colMeans)
	return rowMeans, colMeans
	
def showCircleResult(im,ctrs):
	centers = np.array(ctrs) if type(ctrs)==list else ctrs
	fig = plt.figure(figsize=figsizeSmall)
	ax = fig.add_subplot(1,1,1) #1x1 grid, 1st subplot
	ax.imshow(im,cmap='gray')
	if centers.size>0:
		ax.scatter(x=centers[:,0],y=centers[:,1],s=25,c='red')
	plt.show()

def findGrid(img,diameter,nrow,ncol,region=None,imgName=None,saveImage=False,blurK=None,morphGradientK=None):
	assert (saveImage==False) or ((saveImage==True) and (imgName is not None) and (region is not None))
	
	blurK = max(round2Odd(diameter/1.8),15) if blurK is None else blurK #1.9 previously
	morphGradientK = max(round2Odd(diameter/5.0),9) if morphGradientK is None else morphGradientK #divide by 5 previously
	#print blurK,morphGradientK
	
	blurred = cv2.medianBlur(cv2.cvtColor(img,cv2.COLOR_BGR2GRAY),blurK)
	gradient_kernel = cv2.getStructuringElement(cv2.MORPH_ELLIPSE,(morphGradientK,morphGradientK))
	gridImage = 255 - cv2.morphologyEx(blurred,cv2.MORPH_GRADIENT,gradient_kernel,iterations=1)
	
	rowMeans = []
	for row in gridImage:
		rowMeans.append(np.mean(row))
	colMeans = []
	for col in gridImage.T:
			colMeans.append(np.mean(col))

	paramsRange = [2.5,1.9,2.1,2.3,2.7,2.9,3.1,3.3] #added 1.9 and 3.3
	for p in paramsRange:
		rowFiltered = gaussian_filter1d(rowMeans, diameter/p, axis=-1, order=0, output=None, mode='reflect', cval=0.0, truncate=4.0) #previously 2.5
		rowMinima = argrelextrema(rowFiltered, np.less)[0]
		numRowMinimaFound = len(rowMinima)
		if numRowMinimaFound==nrow:
			break
	for p in paramsRange:
		colFiltered = gaussian_filter1d(colMeans, diameter/p, axis=-1, order=0, output=None, mode='reflect', cval=0.0, truncate=4.0) #previously 2.5
		colMinima = argrelextrema(colFiltered, np.less)[0]
		numColMinimaFound = len(colMinima)
		if numColMinimaFound==ncol:
			break

	if saveImage:
		fig = plt.figure(figsize=figsizeLarge)
		ax = fig.add_subplot(2,2,1) #2x1 grid, 1st subplot
		ax.plot(rowMeans)

		ax = fig.add_subplot(2,2,3) #2x1 grid, 1st subplot
		ax.plot(rowFiltered)
		
		ax = fig.add_subplot(2,2,2) #2x1 grid, 1st subplot
		ax.plot(colMeans)
		
		ax = fig.add_subplot(2,2,4) #2x1 grid, 1st subplot
		ax.plot(colFiltered)
		fig.savefig(imgName+'/'+imgName+"_"+region+'GridSignal.jpg')
		plt.close(fig)	
	
	colMinima = argrelextrema(colFiltered, np.less)[0]

	imgL,imgW = gridImage.shape
	for minimum in np.nditer(rowMinima):
		cv2.line(gridImage, (0,minimum), (imgW-1,minimum), (0, 0, 255), 3, cv2.LINE_4)
		
	for minimum in np.nditer(colMinima):
		cv2.line(gridImage, (minimum,0), (minimum,imgL-1), (0, 0, 255), 3, cv2.LINE_4)
	
	if numRowMinimaFound==nrow and numColMinimaFound==ncol:
		return 0,rowMinima,colMinima,gridImage
	return 2,rowMinima,colMinima,gridImage

def find_nearest(arr,val):
	idx = (np.abs(arr-val)).argmin()
	return arr[idx]

def distance(p0, p1):
	return sqrt((p0[0] - p1[0])**2 + (p0[1] - p1[1])**2)

def getCentroids(rowLines,colLines,nrows,ncols,ctrs,diameter): #check if existing detected holes are good, return list of good centroids, and a percentage of holes that were detected
	centroidCopy = ctrs[:,0:2].tolist()
	if nrows*ncols > rowLines.size*colLines.size:
		print ("Sanity check failed, too few")
		return [],[]
	elif nrows*ncols < rowLines.size*colLines.size:
		print ("Sanity check failed, too many")
		return [],[]
	gridPts = [(x,y) for y in rowLines for x in colLines] #remove values as we find suitable centroids
	for ctr in ctrs: #(x,y,r)
		closestRow=find_nearest(rowLines,ctr[1])
		closestCol=find_nearest(colLines,ctr[0])
		dist = distance((ctr[0],ctr[1]),(closestCol,closestRow))
		if dist<diameter/3.0:
			gridPts.remove((closestCol,closestRow))
		else:
			centroidCopy.remove([ctr[0],ctr[1]])
	#print 'Good:',len(centroidCopy)
	#print 'Filled in:',len(gridPts)
	return centroidCopy,gridPts

def gridPositions(centroids,nrow,ncol): #take centroids, cast to int, determine what row and column it is (e.g. hole at 2nd row, 5th column)
	rowColLookup = {}
	centroids = sorted([(int(centroid[0]),int(centroid[1])) for centroid in centroids],key = lambda x:x[1]) #sort by y (row)
	for idx,centroid in enumerate(centroids):
		rowColLookup[centroid] = [(idx//ncol)+1]
	centroids = sorted(centroids,key = lambda x:x[0]) #sort by x (column)
	for idx,centroid in enumerate(centroids):
		rowColLookup[centroid].append((idx//nrow)+1)
	return centroids,rowColLookup

def cropHoles(img,centroids,size,diameter,lookup):
	multiplier=1.0
	if len(img.shape)==3:
		rows,cols,channels=img.shape
	else:
		rows,cols=img.shape
	
	images = {}
	for centroid in centroids:
		rowStart = max(centroid[1]-int(diameter*multiplier),0)
		rowEnd = min(centroid[1]+int(diameter*multiplier),rows)
		colStart = max(centroid[0]-int(diameter*multiplier),0)
		colEnd = min(centroid[0]+int(diameter*multiplier),cols)
		tmpImg = img[rowStart:rowEnd, colStart:colEnd]
		images[lookup[centroid][0],lookup[centroid][1]] = tmpImg
	return images

def saveCropped(imgName,size,images):
	createDirectory(imgName+'/individualHoles')
	for k in images:
		cv2.imwrite(imgName+'/individualHoles/'+imgName+'_'+size+'Hole_'+str(k[0])+'_'+str(k[1])+'.jpg',images[k])

def resizeImage(d,newD,filename,size,bw):
	im = cv2.imread(d+'/'+filename)
	im = cv2.resize(im,(size,size), interpolation=cv2.INTER_AREA)
	if bw:
		im = cv2.cvtColor(im,cv2.COLOR_BGR2GRAY)
	normim = np.copy(im)
	normim = cv2.normalize(im,normim,0,255,cv2.NORM_MINMAX)
	cv2.imwrite(newD+'/'+filename,normim)
	return filename

def normalizeFeatures(im): #normalize features to -0.5 to 0.5
	return (im - 255.0 / 2) / 255.0

def useClassifier(clf,images,region,nrow,ncol): #predict hole or screw for each image from region and return nrow x ncol array of result
	size = 28
	result = {}
	for k in images:
		im = images[k]
		row = k[0]
		col = k[1]
		im = cv2.resize(im,(size,size),interpolation=cv2.INTER_AREA)
		im = cv2.cvtColor(im,cv2.COLOR_BGR2GRAY)
		normim = np.copy(im)
		normim = normalizeFeatures(cv2.normalize(im,normim,0,255,cv2.NORM_MINMAX))
		result[row,col] = clf.predict(normim.reshape(1,size*size))[0]
	return result


def saveResultViz(mlResult,imgName,region,nrow,ncol):
	px_r = (nrow+1)*100
	px_c = (ncol+1)*100
	im = np.zeros((px_r,px_c,3), np.uint8)+220
	for k in mlResult:
		loc_r = k[1]*100
		loc_c = k[0]*100
		if mlResult[k]==0:
			cv2.circle(im, (loc_r,loc_c), 25, (50, 0, 0), thickness=5, lineType=cv2.LINE_4)
		else:
			cv2.circle(im, (loc_r,loc_c), 30, (0, 0, 255), thickness=-1, lineType=cv2.LINE_4)

	cv2.imwrite(imgName+"/"+region+"_result.jpg",im)

def transformImage(img,tW,ul,ur,br,bl): #use corners to transform tray
	corners = np.array([list(ul),list(ur),list(br),list(bl)],dtype = "float32")

	#tray aspect ratio different from picture aspect ratio
	#tray is 24cm wide, 16cm long
	ratio = 16.3/24
	w=tW #~4MP
	#w=3800 #~8MP
	h=int(w*ratio)
	newcorners = np.array([[0,0],[w,0],[w,h],[0,h]],dtype = "float32")

	M = cv2.getPerspectiveTransform(corners, newcorners)
	result = cv2.warpPerspective(img, M, (w,h))
	return result

def saveTrayDetect(img,imgName,ul,ur,br,bl): #save image of detected tray
	trayDetect = np.copy(img)
	cv2.line(trayDetect, ul, ur, (0, 0, 255), 10, cv2.LINE_4)
	cv2.line(trayDetect, ur, br, (0, 0, 255), 10, cv2.LINE_4)
	cv2.line(trayDetect, br, bl, (0, 0, 255), 10, cv2.LINE_4)
	cv2.line(trayDetect, bl, ul, (0, 0, 255), 10, cv2.LINE_4)
	cv2.imwrite(imgName+"/"+imgName+"_tray_detection.jpg",trayDetect)

def saveTransformed(t_img,imgName,regionD): #tuned for roughly 2208x1500 resolution
	splitRegions = np.copy(t_img)
	cv2.rectangle(splitRegions, regionD[1]['ul'], regionD[1]['br'], (0, 0, 255), thickness=3, lineType=cv2.LINE_4) #4MP
	cv2.rectangle(splitRegions, regionD[2]['ul'], regionD[2]['br'], (0, 0, 255), thickness=3, lineType=cv2.LINE_4) #4MP
	cv2.rectangle(splitRegions, regionD[3]['ul'], regionD[3]['br'], (0, 0, 255), thickness=3, lineType=cv2.LINE_4) #4MP
	cv2.imwrite(imgName+"/"+imgName+"_transformed.jpg",splitRegions)
	
def saveHoughResult(h_img,centers,region,imgName,regionParams):
	houghResult = cv2.cvtColor(np.copy(h_img),cv2.COLOR_GRAY2BGR)
	for ctr in centers:
		cv2.circle(houghResult, (int(ctr[0]),int(ctr[1])), 4, (0, 0, 255), thickness=6, lineType=cv2.LINE_4)
	
	cv2.imwrite(imgName+"/"+imgName+"_"+region+"HoughResult.jpg",houghResult)
	
def saveGridAndHoughResult(g_img,centers,region,imgName,regionParams):
	gridAndHoughResult = cv2.cvtColor(np.copy(g_img),cv2.COLOR_GRAY2BGR)
	for ctr in centers:
		cv2.circle(gridAndHoughResult, (int(ctr[0]),int(ctr[1])), 4, (0, 0, 255), thickness=6, lineType=cv2.LINE_4)
	
	cv2.imwrite(imgName+"/"+imgName+"_"+region+"GridAndHoughResult.jpg",gridAndHoughResult)

def runAlgorithm(img, baseline=None,  overrideHoles=None, saveFiles=False,imgName=None,speedup=True): #algorithm is specific to this tray
	#img is the image that is taken by the user with their phone
	#baseline is the tray/screw layout as defined by the manufacturer.

	if saveFiles:
		saveDirectory = createDirectory(imgName=imgName) #create directory to hold debug/images
		print ("Saving to:",saveDirectory+"/")
		cv2.imwrite(saveDirectory+"/"+saveDirectory+"original.jpg",img)
	
	#find edges of tray to standardize to rectangle/fix keystone effect
	edgeDetectStatus,u_left, u_right, b_right, b_left = find_tray(img,speedup)
	if saveFiles:
		saveTrayDetect(img,saveDirectory,u_left,u_right,b_right,b_left)
	if edgeDetectStatus != 0:
		return edgeDetectStatus,None,None,None #0 means success, 1 means failure at edge detection, 2 means failure in grid detection

	#transform image
	regionParamsDict = { #assuming transformed image is 2208 pixels wide
		1:{'diameter':50,'numRow':10,'numCol':24,'ul':(85,40),'br':(2115,880),'name':'large'},
		2:{'diameter':39,'numRow':8,'numCol':18,'ul':(85,910),'br':(1425,1440),'name':'medium'},
		3:{'diameter':24,'numRow':8,'numCol':16,'ul':(1409,1015),'br':(2170,1420),'name':'small'} #1405, 1409 work, 1415 doesn't work, #2175->2170
	}
	transformedW = 2208 #3-4MP
	transformed = transformImage(img,transformedW,u_left,u_right,b_right,b_left)
	if saveFiles:
		saveTransformed(transformed,saveDirectory,regionParamsDict) #overlay regions on transformed image
	markedImage = np.copy(transformed)
	fullResults = [] #each hole is marked as "Removed", "Present", "empty", or "other"
	usedScrews =[] #just the holes where screws were used

	#process baseline
	alphabet="ABCDEFGHIJ"
	baselineDict = {1:{},2:{},3:{}}
	for hole in baseline:
		rowAlpha = hole['HOLE_NUMBER'][0]
		row = alphabet.index(rowAlpha)+1
		col = int(hole['HOLE_NUMBER'][1:])
		baselineDict[hole['TRAY_GROUP']][row,col] = 0 if hole['SCREW_ID']=='0' else 1

	#process override
	overrideDict = {1:{},2:{},3:{}}
	overrideFlag = False
	if overrideHoles:
		overrideFlag = True
		for hole in overrideHoles:
			rowAlpha = hole['HOLE_NUMBER'][0]
			row = alphabet.index(rowAlpha)+1
			col = int(hole['HOLE_NUMBER'][1:])
			overrideDict[hole['TRAY_GROUP']][row,col] = 0 if hole['SCREW_ID']==0 else 1

	clf = joblib.load('app/classifier_SVC.pkl')
	for region in [1,2,3]:
		regionParams = regionParamsDict[region]
		regionText = regionParams['name']
		imgRegion = transformed[regionParams['ul'][1]:regionParams['br'][1],regionParams['ul'][0]:regionParams['br'][0]]
		if saveFiles:
			cv2.imwrite(saveDirectory+"/"+saveDirectory+"_"+regionText+"Holes.jpg",imgRegion)
		resImg,houghInput,centers = findCircles(imgRegion,'morphGradient',regionParams['diameter'])
		if saveFiles:
			saveHoughResult(houghInput,centers,regionText,saveDirectory,regionParams)

		if saveFiles:
			gridDetectStatus,rows,cols,gridImage = findGrid(imgRegion,regionParams['diameter'],regionParams['numRow'],regionParams['numCol'],region=regionText,imgName=saveDirectory,saveImage=True)
		else:
			gridDetectStatus,rows,cols,gridImage = findGrid(imgRegion,regionParams['diameter'],regionParams['numRow'],regionParams['numCol'],region=regionText)
		if gridDetectStatus != 0:
			if speedup:
				return runAlgorithm(img, baseline,  overrideHoles, saveFiles,imgName,speedup=False)
			else:
				return gridDetectStatus,None,None,None #0 means success, 1 means failure at edge detection, 2 means failure in grid detection

		goodCentroids,missingCentroids = getCentroids(rows,cols,regionParams['numRow'],regionParams['numCol'],centers,regionParams['diameter'])
		if saveFiles:
			saveGridAndHoughResult(gridImage,goodCentroids,regionText,saveDirectory,regionParams)    
	
		combinedCentroids = goodCentroids[:]
		combinedCentroids.extend(missingCentroids)
		centroids,rowColLookup=gridPositions(combinedCentroids,regionParams['numRow'],regionParams['numCol'])
		cropped = cropHoles(imgRegion,centroids,regionText,regionParams['diameter'],rowColLookup)
		if saveFiles:
			saveCropped(saveDirectory,regionText,cropped)
		
		MLres = useClassifier(clf,cropped,regionText,regionParams['numRow'],regionParams['numCol']) #dictionary holding whether a screw was detected at (row,col), one-indexed
		if saveFiles:
			saveResultViz(MLres,saveDirectory,regionText,regionParams['numRow'],regionParams['numCol'])

		#COMPARE RESULTS TO BASELINE
		for centroid in centroids:
			r = rowColLookup[centroid][0]
			c = rowColLookup[centroid][1]
			screw_prev = 1 if baselineDict[region].get((r,c)) else 0 #1 means screw, 0 means no screw
			screw_curr = MLres[r,c]
			regionOverride = overrideDict[region]
			if overrideFlag:
				if regionOverride.get((r,c)) in [0,1]:
					screw_curr = regionOverride[r,c]

			holeID = alphabet[r-1]+str(c)
			if (screw_prev == 1) and (screw_curr ==0): #screw used
				cv2.circle(markedImage, (centroid[0]+regionParams['ul'][0],centroid[1]+regionParams['ul'][1]), regionParams['diameter']//2, (0, 0, 255), thickness=5, lineType=cv2.LINE_4)
				fullResults.append({'HOLE_NUMBER':holeID,'SCREW_STATUS':'Removed','TRAY_GROUP':region})
				usedScrews.append({'HOLE_NUMBER':holeID,'SCREW_STATUS':'Removed','TRAY_GROUP':region})
			elif (screw_prev == 1) and (screw_curr ==1): #screw unused
				cv2.circle(markedImage, (centroid[0]+regionParams['ul'][0],centroid[1]+regionParams['ul'][1]), regionParams['diameter']//2, (0, 255, 0), thickness=5, lineType=cv2.LINE_4)
				fullResults.append({'HOLE_NUMBER':holeID,'SCREW_STATUS':'Present','TRAY_GROUP':region})
			elif (screw_prev == 0) and (screw_curr ==1): #screw moved from another position?
				cv2.circle(markedImage, (centroid[0]+regionParams['ul'][0],centroid[1]+regionParams['ul'][1]), regionParams['diameter']//2, (0, 255, 255), thickness=5, lineType=cv2.LINE_4)
				fullResults.append({'HOLE_NUMBER':holeID,'SCREW_STATUS':'other','TRAY_GROUP':region})
			#else: #otherwise, empty
				#fullResults.append({'HOLE_NUMBER':holeID,'SCREW_STATUS':'empty','TRAY_GROUP':region})

	return 0, markedImage, json.dumps({}), json.dumps(fullResults) #0 means success, 1 means failure at edge detection, 2 means failure in grid detection
