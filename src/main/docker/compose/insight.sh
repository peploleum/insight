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
for DIR in graph/cassandra/data mongodb/data
do
	echo "recreating data directory: $DIR"
	sudo rm -rf $DIR
	mkdir $DIR
	sudo echo '*.*' > $DIR/.gitignore
	sudo echo '/**' >> $DIR/.gitignore
done
docker-compose -f insight.yml up -d --build
;;

run)

echo "starting insight .. using bound volumes"
docker-compose -f insight.yml up -d
;;

stop)

echo "stopping insight .. keeping bound volumes"
docker-compose -f insight.yml down
;;
esac
