# Instructions for starting the Tensorflow Serving docker container

### List all containers, including stopped containers. I think the container name is mdir, used for the next command.
```
sudo docker ps -a
```
### Start the container, -i for interactive
```
sudo docker start -i mdir
```

### Move into Tensorflow Serving directory
```
cd /serving/
```

### Start Tensorflow Serving
```
/serving/bazel-bin/tensorflow_serving/model_servers/tensorflow_model_server --port=9000 --model_name=mdir --model_base_path=/serving/MDIR_Model &> /serving/mdir_log &
```

### Check server is running properly
```
tail mdir_log
```
### then:
Ctrl+p    Ctrl+q

### model files
The model files are in /serving/MDIR_Model/1/
The directories are numbers so Tensorflow Serving can track model versions. The next time a new model is added for the web service, you can make a new folder 2/ and put the new files in there: saved_model.pb and variables/
Kill the old process and start Tensorflow Serving again.
