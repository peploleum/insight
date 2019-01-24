#!/bin/bash

#kubectl create -f namespace.yml
#kubectl create -f volume/
#kubectl create -f syslog_client.yml
#kubectl create -f syslog.yml
#kubectl create -f feeder.yml


#kubectl apply -f namespace.yml
#kubectl apply -f volume/
kubectl apply -f syslog_client.yml
kubectl apply -f syslog.yml
kubectl apply -f feeder.yml