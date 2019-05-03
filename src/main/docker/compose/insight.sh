#!/usr/bin/env bash

if [ $# -lt 1 ]; then
  echo "Usage: freeipa.sh VERB"
  echo
  echo VERB must be one of:
  echo "  • install"
  echo "  • run "
  echo "  • stop "
  echo
  echo "Examples:"
  echo "  • $0 install"
  echo "  • $0 run"
  exit 1
fi

verb=$1
case $verb in
run|install|stop)
  ;;
*)
  echo "error: unknown verb '$verb'"
  exit 1
esac

case $verb in

install)
echo "stopping insight if running"
docker-compose -f insight.yml down
echo "installing insight .. deleting bound volumes"
for DIR in graph/cassandra/data mongodb/data feeder/templates
do
	echo "recreating data directory: $DIR"
	sudo rm -rf $DIR
	mkdir -p $DIR
	sudo echo '*.*' > $DIR/.gitignore
	sudo echo '/**' >> $DIR/.gitignore
done

echo "recreating data directory: ./elk/elasticsearch/data"
sudo rm -rf ./elk/elasticsearch/data
mkdir -p  ./elk/elasticsearch/data

echo "./elk/elasticsearch/data dans .gitignore"
touch ./elk/elasticsearch/data/.gitignore
sudo echo '*.*' > ./elk/elasticsearch/data/.gitignore
sudo echo '/**' >> ./elk/elasticsearch/data/.gitignore

echo "./elk/logstash/in dans .gitignore"
touch ./elk/logstash/in/.gitignore
sudo echo '*.*' > ./elk/logstash/in/.gitignore
sudo echo '/**' >> ./elk/logstash/in/.gitignore

echo "copying NiFi templates"
cp ././../kubernetes/feeder/* feeder/templates
echo "adding exec rights to janus init script"
chmod +x graph/janus/init.sh
echo "demarrage insight"
docker-compose -f insight.yml -p insight up -d --build

#echo "Arret logstash"
tostop=$(docker ps -a | grep logstash | awk '{print $1}')
echo container to stop $tostop
docker stop $tostop

sleep 5

echo "Demarrage logstash"
echo container to start $tostop
docker start $tostop


;;

run)

echo "starting insight .. using bound volumes"
docker-compose -f insight.yml -p insight up -d

echo "Arret logstash pour eviter re index"
tostop=$(docker ps -a | grep logstash | awk '{print $1}')
echo container to stop $tostop
docker stop $tostop

;;

stop)

echo "stopping insight .. keeping bound volumes"
docker-compose -f insight.yml -p insight down
;;
esac
