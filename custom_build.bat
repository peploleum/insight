call mvn package -DskipTests -Pprod verify jib:dockerBuild
docker login --username=peploleum
docker tag insight peploleum/insight:1.0.0
docker push peploleum/insight:1.0.0
