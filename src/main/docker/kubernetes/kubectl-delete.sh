#!/bin/bash
# Files are ordered in proper order with needed wait for the dependent custom resource definitions to get initialized.
# Usage: bash kubectl-delete.sh

kubectl delete -f syslog/ --force --grace-period=0
kubectl delete -f graph/ --force --grace-period=0
kubectl delete -f insight/ner/ --force --grace-period=0
kubectl delete -f feeder/ --force --grace-period=0
kubectl delete -f console/ --force --grace-period=0
kubectl delete -f messagebroker/ --force --grace-period=0
kubectl delete -f insight/ --force --grace-period=0
kubectl delete -f volumes/ --force --grace-period=0
kubectl delete --all pods --namespace=insight --force --grace-period=0
kubectl delete namespace insight --force --grace-period=0
