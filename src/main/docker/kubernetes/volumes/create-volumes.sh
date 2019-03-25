sudo rm -R /home/capgemini/binding/INSIGHT
sudo mkdir -p /home/capgemini/binding
sudo chown -R $USER:$USER /home/capgemini/binding
sudo chmod -R 755 /home/capgemini/binding
mkdir -p /home/capgemini/binding/INSIGHT/nifi/
cp ../feeder/* /home/capgemini/binding/INSIGHT/nifi/
mkdir -p /home/capgemini/binding/INSIGHT/nifi/out
mkdir -p /home/capgemini/binding/INSIGHT/master/data
mkdir -p /home/capgemini/binding/INSIGHT/elasticsearch-console
mkdir -p /home/capgemini/binding/INSIGHT/elasticsearch
mkdir -p /home/capgemini/binding/INSIGHT/logstash/
cp ../insight/logstash/pipeline/* /home/capgemini/binding/INSIGHT/logstash/
mkdir -p /home/capgemini/binding/INSIGHT/mongodb/
mkdir -p /home/capgemini/binding/INSIGHT/graph
cp ../graph/conf/* /home/capgemini/binding/INSIGHT/graph/
cp /home/capgemini/binding/INSIGHT/graph/gremlin-server-configuration.yml /home/capgemini/binding/INSIGHT/graph/gremlin-server-configuration.yaml
mkdir -p /home/capgemini/binding/INSIGHT/cassandra
mkdir -p /home/capgemini/binding/INSIGHT/refgeo/logstash/
mkdir -p /home/capgemini/binding/INSIGHT/refgeo/logstash/csv
cp ../refgeo/logstash/csv/* /home/capgemini/binding/INSIGHT/refgeo/logstash/csv
mkdir -p /home/capgemini/binding/INSIGHT/refgeo/logstash/pipeline
cp ../refgeo/logstash/pipeline/* /home/capgemini/binding/INSIGHT/refgeo/logstash/pipeline
sudo chmod -R 755 /home/capgemini/binding/INSIGHT
