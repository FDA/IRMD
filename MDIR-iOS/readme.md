This is the readme for setting up the iOS app

## Overview

The mobile application to authenticate has capabilities to authenticate user through login, collect image samples and retreive image recognition results from the back end api's. This is a native iOS application and has been developed using Swift programming language.  

## Setting up the iOS application

1. Clone the repository. 
2. Open the iOS project with XCode by opening the MDIR-IOS/ FDA.xcworkspace project.

## Brief Description of Folders
* Pods - This folder contains all the dependencies of the project. 
* FDATests - This folder contains all the files related to testing the project to ensure code coverage. 
* FDA - This folder is the root folder containing all the application code. 
* FDA/Model - This folder contains different Models that have been defined for the project like User, UserSettings, CommonMethods, Constants. The Constants.swift file has all the important constants used in the application. Please make sure to update the baseApiURl, imageRecognition and imageRecognitionTray2 with your api URL's. 
* FDA/WebServices - This consists of all the application code that communicates with the Java Backend API's. 
* Networking - This folder contains base classes required for making network calls. 
* Helper - This folder contains the helper classes for the project. 

