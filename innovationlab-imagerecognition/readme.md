## Overview

The Java backend exposes api's for the mobile application to authenticate users and read and write data to the back end.This application has been developed using SPRING MVC framework 

## Setting up Environment for the Java application

1. Install Eclipse IDE
2. Install and Setup Tomcat Server on your device
3. Generate war file and deploy on tomcat server

## Brief Description of Folders
* Constroller (path:\imagerecognition-web\src\main\java\com\fda\mdir\controller) : This folder contains all the rest and non-rest controllers required for the MDIR application. 
* Controller- nonrest (path:\imagerecognition-web\src\main\java\com\fda\mdir\controller\nonrest): This folder contains the controllers responsible for the user authentication of MDIR application. 
* Controller- rest (path:\imagerecognition-web\src\main\java\com\fda\mdir\controller\rest):  This folder contains the controllers responsible for all communications and rest calls between the iOS application and the back-end. 
* Data/Dto : This folder contains code that acts as a segue between the DAO and the Controllers. 
* Data/serializer: This folder contains code that serializes the data objects 
* dao (path: \imagerecognition-web\src\main\java\com\fda\mdir\db\dao): This folder consists of Java classes that execute queries to pull data from the database. 
*entities (path: \imagerecognition-web\src\main\java\com\fda\mdir\db\entities):  This folder contains models for all entities that are present in the web application. 
* init (path: \imagerecognition-web\src\main\java\com\fda\mdir\init): This folder contains all initialization classes. 
* service (path: \imagerecognition-web\src\main\java\com\fda\mdir\service ): This folder contains various classes that provide services used throughout the application. 
* security (path: \imagerecognition-web\src\main\java\com\fda\mdir\security): This folder implements the security features like user authentication. 
