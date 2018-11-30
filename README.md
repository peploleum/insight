# insight
This application was generated using JHipster 5.2.1, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v5.2.1](https://www.jhipster.tech/documentation-archive/v5.2.1).

## Development

Install dependencies 

    yarn install

Run app

    ./mvnw
    yarn start

### Service workers

Service workers are commented by default, to enable them please uncomment the following code.

* The service worker registering script in index.html

```html
<script>
    if ('serviceWorker' in navigator) {
        navigator.serviceWorker
        .register('./service-worker.js')
        .then(function() { console.log('Service Worker Registered'); });
    }
</script>
```

Note: workbox creates the respective service worker and dynamically generate the `service-worker.js`


### Using angular-cli

You should use [Angular CLI][] to generate some custom client code:

1. Generate a new module
    
    
    ng g m Sources --routing

will generate: 

    CREATE src/main/webapp/app/sources/sources-routing.module.ts (250 bytes)
    CREATE src/main/webapp/app/sources/sources.module.spec.ts (283 bytes)
    CREATE src/main/webapp/app/sources/sources.module.ts (283 bytes)
    
2. Register module in app.module
    
edit app.module.ts add it to imported modules

3. Generate new component

    ng g c sources/sources-manager --module sources/sources.module

will generate few files:

    CREATE src/main/webapp/app/sources/sources-manager.component.html (34 bytes)
    CREATE src/main/webapp/app/sources/sources-manager.component.spec.ts (685 bytes)
    CREATE src/main/webapp/app/sources/sources-manager.component.ts (268 bytes)
    UPDATE src/main/webapp/app/sources/sources.module.ts (377 bytes)


4. Edit newly created route to match component

create file 

    .\insight\src\main\webapp\i18n\en\sources.json
    
edit sources-routing.module.ts and add route like so: 

    const routes: Routes = [{
        path: 'sources',
        component: SourcesManagerComponent,
        data: {
            pageTitle: 'sources.title'
        }
    }];
## Building for production

To optimize the insight application for production, run:

    ./mvnw -Pprod clean package

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

    java -jar target/*.war

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

## Testing

To launch your application's tests, run:

    ./mvnw clean test

### Client tests

Unit tests are run by [Jest][] and written with [Jasmine][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

    yarn test

UI end-to-end tests are powered by [Protractor][], which is built on top of WebDriverJS. They're located in [src/test/javascript/e2e](src/test/javascript/e2e)
and can be run by starting Spring Boot in one terminal (`./mvnw spring-boot:run`) and running the tests (`yarn run e2e`) in a second one.
### Other tests

Performance tests are run by [Gatling][] and written in Scala. They're located in [src/test/gatling](src/test/gatling).

To use those tests, you must install Gatling from [https://gatling.io/](https://gatling.io/).

For more information, refer to the [Running tests page][].

### Docker-compose

build image
    
    custom_build.bat
    
deploy components

    cd C:\dev\pipe\src\main\docker\compose
    docker-compose  -f .\insight.yml -p insight up -d

### Manage app

* app endpoint: [http://localhost:8080](http://localhost:8080)
* kafka default endpoint: kafka:9092
* kibana endpoint: [http://localhost:5601](http://localhost:5601)
* elasticsearch endpoint: [http://localhost:9200](http://localhost:9200)
* nifi endpoint: [http://localhost:8090](http://localhost:8090)
### kubernetes

Coming soon
# JHipster generated kubernetes configuration

## Preparation

You will need to push your image to a registry. If you have not done so, use the following commands to tag and push the images:

```
$ docker image tag insight 192.168.65.2:8094/insight
$ docker push 192.168.65.2:8094/insight
```

## Deployment

You can deploy all your apps by running the below bash command:

```
./kubectl-apply.sh
```

## Exploring your services


Use these commands to find your application's IP addresses:

```
$ kubectl get svc insight -n insight
```

## Scaling your deployments

You can scale your apps using

```
$ kubectl scale deployment <app-name> --replicas <replica-count> -n insight
```

## zero-downtime deployments

The default way to update a running app in kubernetes, is to deploy a new image tag to your docker registry and then deploy it using

```
$ kubectl set image deployment/<app-name>-app <app-name>=<new-image>  -n insight
```

Using livenessProbes and readinessProbe allows you to tell kubernetes about the state of your apps, in order to ensure availablity of your services. You will need minimum 2 replicas for every app deployment, you want to have zero-downtime deployed. This is because the rolling upgrade strategy first kills a running replica in order to place a new. Running only one replica, will cause a short downtime during upgrades.

## Monitoring tools

### JHipster console

Your application logs can be found in JHipster console (powered by Kibana). You can find its service details by
```
$ kubectl get svc jhipster-console -n insight
```

* If you have chosen *Ingress*, then you should be able to access Kibana using the given ingress domain.
* If you have chosen *NodePort*, then point your browser to an IP of any of your nodes and use the node port described in the output.
* If you have chosen *LoadBalancer*, then use the IaaS provided LB IP



## Troubleshooting

> my apps doesn't get pulled, because of 'imagePullBackof'

check the registry your kubernetes cluster is accessing. If you are using a private registry, you should add it to your namespace by `kubectl create secret docker-registry` (check the [docs](https://kubernetes.io/docs/tasks/configure-pod-container/pull-image-private-registry/) for more info)

> my apps get killed, before they can boot up

This can occur, if your cluster has low resource (e.g. Minikube). Increase the `initialDelySeconds` value of livenessProbe of your deployments

> my apps are starting very slow, despite I have a cluster with many resources

The default setting are optimized for middle scale clusters. You are free to increase the JAVA_OPTS environment variable, and resource requests and limits to improve the performance. Be careful!


> my SQL based microservice stuck during liquibase initialization when running multiple replicas

Sometimes the database changelog lock gets corrupted. You will need to connect to the database using `kubectl exec -it` and remove all lines of liquibases `databasechangeloglock` table.

## Saving images

    docker save -o insight-bundle.tar postgres:10.4 busybox:latest insight:latest elasticsearch:5.6.5 wurstmeister/kafka:latest wurstmeister/zookeeper:latest jhipster/jhipster-logstash:v3.0.1 jhipster/jhipster-console:v3.0.1 jhipster/jhipster-elasticsearch:v3.0.1 jhipster/jhipster-import-dashboards:v3.0.1

## Loading images

Login to the docker engine host. Load archive:

    sudo docker load --input insight-bundle.tar
    
## Tagging and pushing images

Log into registry 

    sudo docker login -u admin -p ****** 192.168.65.5:8093
    
Tag and push

    sudo docker tag postgres:10.4 192.168.65.5:8093/postgres:10.4
    sudo docker push 192.168.65.5:8093/postgres:10.4
    sudo docker tag busybox 192.168.65.5:8093/busybox:latest
    sudo docker push 192.168.65.5:8093/busybox:latest
    sudo docker tag insight 192.168.65.5:8093/insight:latest
    sudo docker push 192.168.65.5:8093/insight:latest
    sudo docker tag elasticsearch:5.6.5 192.168.65.5:8093/elasticsearch:5.6.5
    sudo docker push 192.168.65.5:8093/elasticsearch:5.6.5
    sudo docker tag wurstmeister/kafka 192.168.65.5:8093/wurstmeister/kafka:latest
    sudo docker push 192.168.65.5:8093/wurstmeister/kafka:latest
    sudo docker tag wurstmeister/zookeeper 192.168.65.5:8093/wurstmeister/zookeeper:latest
    sudo docker push 192.168.65.5:8093/wurstmeister/zookeeper:latest
    sudo docker tag jhipster/jhipster-logstash:v3.0.1 192.168.65.5:8093/jhipster/jhipster-logstash:v3.0.1
    sudo docker push 192.168.65.5:8093/jhipster/jhipster-logstash:v3.0.1
    sudo docker tag jhipster/jhipster-console:v3.0.1 192.168.65.5:8093/jhipster/jhipster-console:v3.0.1
    sudo docker push 192.168.65.5:8093/jhipster/jhipster-console:v3.0.1
    sudo docker tag jhipster/jhipster-elasticsearch:v3.0.1 192.168.65.5:8093/jhipster/jhipster-elasticsearch:v3.0.1
    sudo docker push 192.168.65.5:8093/jhipster/jhipster-elasticsearch:v3.0.1
    sudo docker tag jhipster/jhipster-import-dashboards:v3.0.1 192.168.65.5:8093/jhipster/jhipster-import-dashboards:v3.0.1
    sudo docker push 192.168.65.5:8093/jhipster/jhipster-import-dashboards:v3.0.1

    
      
## Janus graph settings

### Start the app

    cd PROJECT_ROOT\src\main\docker\compose\graph
    
    docker-compose -f .\graph.yml -p janusgraph -d --build
    
### Create graph from the command line

    curl -X POST -d "{\"gremlin\":\"map = new HashMap();map.put('storage.backend', 'cql');map.put('storage.hostname', 'cassandra');map.put('graph.graphname', 'example');ConfiguredGraphFactory.createConfiguration(new MapConfiguration(map));\"}" "http://localhost:8182"
    curl -X POST -d "{\"gremlin\":\"def graph=ConfiguredGraphFactory.open('example'); GraphOfTheGodsFactory.loadWithoutMixedIndex(graph,true);\"}" "http://localhost:8182"
    curl -X POST -d "{\"gremlin\":\"def g=ConfiguredGraphFactory.open('example').traversal(); def saturn=g.V().has('name', 'saturn').next()\"}" "http://localhost:8182"
    ## ajout de vertex ##
    curl -X POST -d "{\"gremlin\":\"def g=ConfiguredGraphFactory.open('example').traversal();def v1 = g.addV(); def p1=v1.property('name', 'Nicolas')\"}" "http://localhost:8182"
    curl -X POST -d "{\"gremlin\":\"def g=ConfiguredGraphFactory.open('example').traversal();def nicolas=g.V().has('name', 'Nicolas').next()\"}" "http://localhost:8182"
     
    
### CRUD Vertices and Edges from java client


