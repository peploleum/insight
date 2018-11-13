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

You can also use [Angular CLI][] to generate some custom client code.

For example, the following command:

    ng generate component my-component

will generate few files:

    create src/main/webapp/app/my-component/my-component.component.html
    create src/main/webapp/app/my-component/my-component.component.ts
    update src/main/webapp/app/app.module.ts


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

    cd C:\dev\pipe\src\main\docker\prod
    docker-compose  -f .\insight.yml -p insight up -d

### Manage app

app endpoint: [http://localhost:8080](http://localhost:8080)

kafka default endpoint: kafka:9092

kibana endpoint: [http://localhost:5601](http://localhost:5601)

elasticsearch endpoint: [http://localhost:9200](http://localhost:9200)

nifi endpoint: [http://localhost:8090](http://localhost:8090)
### kubernetes

Coming soon
