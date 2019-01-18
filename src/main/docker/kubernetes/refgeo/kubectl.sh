#!/bin/bash

#kubectl create -f namespace.yml
#kubectl create -f volume/
#kubectl create -f refgeo_elasticsearch.yml
#kubectl create -f refgeo_kibana.yml
#kubectl create -f refgeo-logstash.yml


#kubectl apply -f namespace.yml
#kubectl apply -f volume/
kubectl apply -f refgeo_elasticsearch.yml
kubectl apply -f refgeo_kibana.yml
kubectl apply -f refgeo-logstash.yml