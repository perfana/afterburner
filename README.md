# afterburner

Simple self-running test jar to use in load test environments to tune and explore monitor and analysis tools.

[![Build Status](https://travis-ci.com/stokpop/afterburner.svg?branch=master)](https://travis-ci.com/stokpop/afterburner)

# afterburner-java

# start
Start in the afterburner-java directory:
    
    mvn spring-boot:run
    
Or build or download an executable jar and run with java, so you can also change jvm settings such as the garbage collector:

    java -jar target/afterburner-java-1.1-SNAPSHOT-exec.jar
    
With gc log enabled:
    
    java -jar -Xlog:gc:file=gc.log:time,uptime target/afterburner-java-1.1-SNAPSHOT-exec.jar 
    
Download a ready build afterburner here: https://github.com/stokpop/afterburner/releases 

# test
Default port number is 8080, so test if it is working using curl:
    
    curl http://localhost:8080/delay
    
Most examples below can be run with curl. Or check out swagger for the calls with parameters.
    
# swagger
The swagger-ui is here after starting afterburner:

    http://localhost:8080/swagger-ui/

# profiles

Two profiles can be set via `spring.profiles.active`:
* `employee-db` activates the employee database controller (only do so when the mariadb employee database is running)
* `logstash` activates logstash in logback on `logstash:9600` (only activate when logstash is available) 

For logstash the following properties (or env vars) can be set:
* `afterburner.logback.logstash.remotehost` 
* ``

# functions

## delay
Call an endpoint that will delay a call for the given duration. Default duration is 100 milliseconds.
The duration can either be given in milliseconds or in [ISO 8601 duration](https://en.wikipedia.org/wiki/ISO_8601#Durations) by starting with P.

If you increase the response time high enough, you are likely to see timeout issues.

Examples:
* `/delay?duration=PT1.5s` delay for 1500 millis
* `/delay?duration=150` delay for 150 millis

## memory

Call this endpoint to increase memory usage to simulate a memory leak or to generate lots of young objects for each request (churn). 

If you increase the memory usage continuously or with a high number of objects you will likely see a OutOfMemory related issues.

Increasing the number of short lived objects (objects that are alive only during a request) you can simulate a high
memory churn rate and see how the garbage collector behaves.

Examples for memory leak:
* `/memory/grow` grows an in memory structure with default values
* `/memory/grow?objects=5&items=5` grows structure with the given values (5 objects with 5 items each)
* `/memory/clear` clears the memory structure and memory will be freed

Examples for memory churn:
* `/memory/churn` creates lots of objects and sleeps for a while
* `/memory/churn?objects=1818&duration=200` creates 1818 objects and sleeps for 200 milliseconds

## cpu

Call this endpoint to burn some cpu cycles using matrix calculations.
You can perform an identity check on a simple magic square. 

Be careful with sizes above 500, 
the response times and cpu effort will increase significantly above these sizes.

Note that the current implementation is doing the calculation within one thread (core).
You can exercise multiple cores by calling multiple concurrent requests.

Examples:
* `/cpu/magic-identity-check` checks identity and simple magic square multiplication with default size 10
* `/cpu/magic-identity-check?matrixSize=100` check for size 100, currently takes around 100 milli sec on a simple laptop
* `/cpu/magic-identity-check?matrixSize=600` check for size 600, currently takes 2.5 sec on a simple laptop
* `/cpu/magic-identity-check?matrixSize=0` get a 500 error (might also be handy for testing)

## remote call

Call another remote http(s) endpoint. Use `path` to specify the path to be called on the remote
instance. 

The base url is specified via the `afterburner.remote.call.base_url` property. The default value is
`http://localhost:8080` so it will call itself.

The default remote client is Apache HttpClient. With the `type` request parameter you can switch to OkHttp.

Examples:
* `/remote/call?path=delay` call delay on remote afterburner via HttpClient
* `/remote/call?path=delay&type=okhttp` call delay on remote afterburner via OkHttp
* `/remote/call?path=remote/call?path=delay` call remote afterburner to call remote afterburner to call delay
* `/remote/call?type=okhttp&path=remote/call?path=delay` same, but first call via OkHttp and second via HttpClient

note: do not forget to escape & with a backslash using bash and curl

A custom metric is available, counting the total number of remote calls:

* http://localhost:8080/actuator/metrics/afterburner.remote.calls

## remote call async

Call many remote http(s) endpoint in parallel using `@Async` method.

Works the same as the remote call above with the addition of how many times
the remote call should be made. All calls are `@Async` calls, using `CompletableFuture`.

Example:
* `/remote/call-many?count=10` call delay on remote afterburner via HttpClient.
* `/remote/call-many?count=200&path=/delay?duration=PT2S` call delay of 2 seconds on remote afterburner via HttpClient.
* `/remote/call-many?count=4&path=/cpu/magic-identity-check` call 4 times the magic-identity-check in parallel on remote afterburner

A custom thread executor pool is created that also registers itself with micrometer.
This way the executor can be monitored on tasks in the queue and number of threads in use.

## upload file

Upload a file to the java tmp directory (be careful not to fill that directory :-).

Example:
* `curl --trace-ascii - -F 'upload=@pom.xml' http://localhost:8080/files/upload` upload the pom.xml file
* `/files/download/pom.xml` download the pom.xml file after a succeeded upload

## database connect

Show latency of connecting to a database using a simple query on a Springboot template.

The default query is 'SELECT 1' and can be changed via the `afterburner.database.connect.query` property.

Afterburner connects to the mysql employees test database on default port 3306 on localhost.

Example:
* `/db/connect` perform simple SELECT 1 to measure base performance to database

Example output:
   
    {"message":"{ 'db-call':'success','query-duration-nanos':447302064 }","name":"Afterburner-One","durationInMillis":447}

## remote database

Call a remote MariaDB database with the MySql employee test database loaded.

* `/db/employee/find-by-name?firstName=Anneke` find employees by first name
* `/db/employee/find-by-name?lastName=Preusig` find employees by last name
* `/db/employee/find-by-name?firstName=Anneke&lastName=Preusig` find by first and last name

Other first names to try: Steen, Aamer, Guoxiang (via `SELECT DISTINCT e.first_name FROM employees.employees e`)

Example output:

    [{"empNo":10006,"birthDate":"1953-04-20","firstName":"Anneke","lastName":"Preusig","gender":"F","hireDate":"1989-06-02"},{"empNo":10640,"birthDate":"1958-11-09","firstName":"Anneke","lastName":"Meszaros" ... 

See dependencies section how to set up the database component.

There is also a query with configurable long delay. Example call:

* `time curl localhost:8080/db/employee/select-long-time\?durationInSec=10`

Use this end-point for instance to test overloaded connection pools and timeout behaviour.
    
## tcp connect

Show latency to remote TCP port using a Java TCP Socket creation.
This closely matches the network latency to the remote site, but includes 
some Java related overhead, such as possible gc times and Java security manager
checks in the Socket code.

Example: 
* `/tcp/connect?host=www.google.com\&port=80\&timeout-ms=1000`

Example output:

    {"message":"{ 'tcp-connect':'success', 'connect-duration-nanos':5281882, 'close-duration-nanos':39150, 'host':'www.google.com', 'port':80 }","name":"Afterburner-One","durationInMillis":6}

## parallel

The parallel call will determine prime numbers via a parallel stream, which uses the implicit ForkJoinPool.

Use this to test behaviour of a busy FJP. 

Example:
* `/parallel` runs prime number check in parallel using the common join fork pool
* `/parallel-info` print metrics of the common join fork pool

## basket validation

Use this basket validation for concurrency issues with shared data in multiple threads.
 
 * `/basket/purchase` - post a basket purchase, the request will be validated on total price and products/prices
 
Good request (10 + 20 + 30 = 60):

    curl -H "Content-Type: application/json" -d '{ "customer": "Johnny", "prices": [10, 20, 30], "products": ["apple", "banana","oranges"], "totalPrice": 60 }' localhost:8080/basket/purchase

Bad request with validation errors (20 + 30 != 40):

    curl -H "Content-Type: application/json" -d '{ "customer": "BadGuy", "prices": [20, 30], "products": ["sushi", "icescream"], "totalPrice": 40 }' localhost:8080/basket/purchase

Put under load and check if all validates as expected.

## basket database store

You can store baskets in a database by calling:

    curl -H "Content-Type: application/json" -d '{ "customer": "Johnny", "prices": [10, 20, 30], "products": ["apple", "banana","oranges"], "totalPrice": 60 }' localhost:8080/basket/store
    
And retrieve all baskets via:

    curl localhost:8080/basket/all

## autonomous worker

Use the autonomous worker to investigate the behaviour of `@Schedule`.

Two methods are in place, one with a fixedRate schedule and one with a fixedDelay schedule.

See what happens when a thread freezes or fails by setting `afterburner.autonomous.worker.stability` to false.

Activate the logging of these workers with property: `logging.level.nl.stokpop.afterburner.service.AutonomousWorker=debug`

## flaky calls and retries

To cover-up downstream flaky services you can make use of retries. For instance by using the resilience4j library.

A flaky service is available here:

* `flaky\?maxRandomDelay=2000\&flakiness=25` will pick a random delay from 1 to 2000 milliseconds, and will respond with a 500 error status 25% of the time (default is 50% flakiness).

If you set the socket or read timeout of the incoming call to 1000 milliseconds, you will also get timeouts 50% of the calls.

To wrap a flaky call like this with retries, use the following call:

* `localhost:8080/remote/call-retry\?path=flaky\?maxRandomDelay=2000` do a remote call to the baseUrl with the given path and also retry 10 times 
when failures like timeouts and 500's occur

It will take longer, but it will succeed more often.

The retry metrics are available via actuator:

* http://localhost:8080/actuator/retries
* http://localhost:8080/actuator/retryevents/afterburner-retry

## connection timeouts and retries

To test connections timeouts when a service is unavailable (e.g. network hickup or remote restart), use this 
"traffic-light" endpoint:

* `curl -v localhost:5599`

It return a green light during 5 seconds and then goes offline for 5 seconds.

To test this with retries:

* `curl localhost:8080/remote/call-traffic-light`

Check the retry metrics via:

* http://localhost:8080/actuator/retryevents/traffic-light-retry

Change the traffic light port with property: `afterburner.trafficlight.port`

## security filter

The `secure-delay` has a BasicAuthenticationFilter enabled with BCrypt check.

Check what impact this has on latency and cpu usage.

Example call:

* `time curl -u pipo:test123 localhost:8080/secured-delay`

# load test
To run a gatling load test, go to the `afterburner-loadtest-gatling` directory and run:

    mvn events-gatling:test
    
To run a jmeter load test, go to the `afterburner-loadtest-jmeter` directory and run:

    mvn clean verify
     
# properties
* `-Dspring.application.name=Angry-Afterburner` provide a name for the instance
* `-Dafterburner.remote.call.base_url=https://my.remote.site:1234` connect remote calls to this base url
* `-Dafterburner.autonomous.worker.stability=false` default the autonomous worker is stable, make it unstable by setting this to false
* `export AFTERBURNER_REMOTE_CALL_BASE_URL=https://my.remote.site:1234` provide base url via environment variable
* `--server.port=8090` use different port (default 8080)
* `export SERVER_PORT=8090` use different port via env variable

## dependencies

# tracing

Run a Jaeger instance to see the tracing.

For example via docker:

    docker run -d --name jaeger \
      -e COLLECTOR_ZIPKIN_HTTP_PORT=9411 \
      -p 5775:5775/udp \
      -p 6831:6831/udp \
      -p 6832:6832/udp \
      -p 5778:5778 \
      -p 16686:16686 \
      -p 14268:14268 \
      -p 14250:14250 \
      -p 9411:9411 \
      jaegertracing/all-in-one:1.20

Use `spring.zipkin.enabled=true` to enable sending data to Jaeger.
Also set property `spring.sleuth.sampler.probability` higher than 0, 
set to 1 to capture all traces, or 0.1 for 10% of the traces.
      
Then see traces here: http://localhost:16686/ 

# prometheus

The prometheus endpoint: http://localhost:8080/actuator/prometheus

# database

Run MariaDB with the MySql employees database:

    docker run -d --name mariadbtest \
      -e MYSQL_ROOT_PASSWORD=mypass \
      -v /path/to/git/test_db:/db \
      -p 3306:3306 \
      mariadb:10.5.5

Clone https://github.com/datacharmer/test_db into /path/to/git/

    git clone https://github.com/datacharmer/test_db.git
    
Then ssh into this docker:
    
    docker exec -it mariadbtest /bin/bash

And load the test database:
    
    cd /db
    mysql -t -p  < employees.sql

And provide password: `mypass`

# docker

To create a docker image, from the `afterburner-java` directory locally:

    ../mvnw clean package jib:dockerBuild 

or to push docker remotely:

    ../mvnw clean package jib:build 

(add `-DskipTests` to speed it up, if you know what you are doing 😉)

If you jib multiple times with same (SNAPSHOT) label, use `docker pull <image-path>` to get most recent version.

##### credits
* fire favicon from [freefavicon](http://www.freefavicon.com)
