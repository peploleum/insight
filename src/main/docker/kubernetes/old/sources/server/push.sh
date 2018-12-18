sudo docker login -u admin -p admin123 192.168.65.5:8093
sudo docker build -t 192.168.65.5:8093/rsyslog/server .
sudo docker push 192.168.65.5:8093/rsyslog/server
