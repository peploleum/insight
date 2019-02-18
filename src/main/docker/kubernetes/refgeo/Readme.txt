Attention au binding de volume

dans /volume/refgeo-logstash-volume.yml
le local storage est a crée sur le node

Attention au changement de chemin dans le repertoire logstash par rapport a gazetter
et logstash.conf
les csv sont dans le repertoire csv, (plus dans in)

pour les services en nodePort, l'IP a mettre avant les port est celle du node (pour la voir, clique sur le node dans la dashboard)

commande kubectl utils:

kubectl get pods --namespace=refgeo

executer un bash sur un pod
kubectl exec -i refgeo-logstash-f5d87655d-752fh -t --namespace=refgeo -- /bin/bash 


kubectl get pod refgeo-logstash-f5d87655d-752fh --namespace=refgeo

kubectl get deployment --namespace=refgeo