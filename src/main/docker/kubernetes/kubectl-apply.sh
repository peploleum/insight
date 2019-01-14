#!/bin/bash
# Files are ordered in proper order with needed wait for the dependent custom resource definitions to get initialized.
# Usage: bash kubectl-apply.sh

kubectl apply -f namespace.yml
kubectl apply -f volumes/
#kubectl apply -f insight/
#kubectl apply -f messagebroker/
#kubectl apply -f console/
#kubectl apply -f feeder/
#kubectl apply -f insight/ner/insight-ner-language-identifier.yml
#kubectl apply -f insight/ner/insight-ner-tokeniser.yml
#kubectl apply -f insight/ner/insight-ner-pos-tagger.yml
#kubectl apply -f insight/ner/insight-ner-docker.yml
#kubectl apply -f insight/ner/insight-ner-kaf2json.yml
#kubectl apply -f insight/ner/insight-ner.yml