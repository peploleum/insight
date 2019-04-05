call mvn package -DskipTests -Pprod -Pgraphy verify jib:dockerBuild
docker login --username=peploleum
docker tag insight peploleum/insight:latest
REM docker push peploleum/insight:1.0.0
