## Overview
This is the image recognition piece of the prototype. When a surgery is complete and a post-surgery image of the tray is taken using the app, computer vision and machine learning are used to detect implants in the image. The results are then compared with the pre-surgery tray assembly to determine implant usage.

## Setup
The algorithms are available as a service built with the [Django](https://www.djangoproject.com/) framework. Set up Phase 1 first, then add Phase 2 as another application and API endpoint. Make sure the necessary Python packages are available in your environment:
* [Django](https://www.djangoproject.com/)
* [OpenCV](https://opencv.org/)
* [matplotlib](https://matplotlib.org/)
* [scikit-learn](https://scikit-learn.org/stable/)
* [numpy](https://numpy.org/)
* [scipy](https://www.scipy.org/)
* [pandas](https://pandas.pydata.org/)
* [tensorflow](https://www.tensorflow.org)
* [tensorflow-serving](https://github.com/tensorflow/serving/tree/master/tensorflow_serving/apis)
* [grpcio](https://pypi.org/project/grpcio/)

For Phase 2, additional setup is required because the object detection model needs to be deployed using [Tensorflow Serving](https://www.tensorflow.org/tfx/guide/serving). Follow the linked documentation to set up a Tensorflow Serving Docker container, move the model file into the container, and start the server.

Once everything is set up, sample scripts are provided to check if the endpoints are working. If everything is working, the mboile application code can be updated to call these endpoints.

## Tray 1 Approach

The approach for tray 1 uses computer vision techniques from [OpenCV](https://opencv.org/) and a support vector classifier (SVC) from [scikit-learn](https://scikit-learn.org/stable/) to determine if there is a screw present in each possible location. First, edge detection finds the edges of the tray. Second, a transformation is applied to make the tray rectangular to account for camera perspective. Third, the tray is cropped into the three regions with different-sized holes. Fourth, for each region, the grid of holes/screws is identified so an image can be cropped for each location. Lastly, a classifier was trained to differentiate between holes and screws so a prediction is made for each possible implant location on the tray.

## Tray 2 Approach

The approach for tray 2 uses object detection. A collection of images with the tray in various arrangements was taken. They were manually labeled using the [LabelImg](https://github.com/tzutalin/labelImg) tool by drawing bounding boxes around the implants, or if the implant was not there, around the implant slot (the picture of the implant on the bottom of the tray). Each implant and slot was its own class. The labels are in XML format and were converted to TFRecord format. Then the [Tensorflow Object Detection API](https://github.com/tensorflow/models/tree/master/research/object_detection) was used to train the model.
