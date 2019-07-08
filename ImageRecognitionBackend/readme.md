# MDIR

### Phase 1
all settings are set up in Phase 1.

### Phase 2 
add another application and api to handle tray2.

## to run server
The server is already been set to run up once instance on.
you can check by:
```
sudo vi /etc/rc.local
```
to manually run it:
```
sudo su
nohup /home/ec2-user/anaconda3/bin/python3.6 /home/ec2-user/server/manage.py runserver 172.31.35.4:80 > log &
```

* the tf-server is also set to run up once instance on by:
```
sudo su
docker run -it -d -P --name mdir --restart always -p 9000:9000 mdir/tf-serving /serving/bazel-bin/tensorflow_serving/model_servers/tensorflow_model_server --port=9000 --model_name=mdir --model_base_path=/serving/MDIR_Model

```