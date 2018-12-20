mvn package -DskipTests -Pprod verify jib:dockerBuild
docker login --username=peploleum
docker tag insight peploleum/insight:latest
docker push peploleum/insight:latest
