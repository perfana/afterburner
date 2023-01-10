# jMeter and Wiremock demo

Generate a jMeter script and wiremock settings and run a load test.

## Steps

Create a demo folder:

    mkdir demo-afterburner
    cd demo-afterburner

Clone afterburner:

    git clone https://github.com/perfana/afterburner.git

Build afterburner:

    cd afterburner/afterburner-java
    ../mvnw package

Start afterburner to get OpenAPI specs (needs java 11+ installed):

    java -jar target/afterburner-java-*-exec.jar

In other terminal, get OpenAPI spec in json:

    curl -S -o afterburner-api.json http://localhost:8080/v3/api-docs

Use this json file as input to wiremock gen and jmeter gen
(docker is needed for these steps):

    docker run --rm -d -p 4756:8080 --name jmeter-gen stokpop/jmeter-gen:0.0.1
    curl -F "file=@afterburner-api.json" http://localhost:4756/upload

The reply is (the id will be different for each run):

    {"projectId":"jmeter-gen.1670182995311"}

With the given id, download the zip file with jmeter script:

    curl -S -o afterburner-jmeter.zip localhost:4756/download/jmeter-gen.1670182995311

Now unzip the file in a separate directory:

    mkdir jmeter
    unzip afterburner-jmeter.zip -d jmeter

Open the file `jmeter/generated-script.jmx` in jmeter.

Now also generate wiremock templates:

    docker run --rm -d -p 4757:8080 --name wiremock-gen stokpop/wiremock-gen:0.0.1
    curl -F "file=@afterburner-api.json" http://localhost:4757/upload

The reply is like:

    {"projectId":"wiremock-gen.1670185260968"}

Use this projectId to download the zip with wiremock mappers:

    curl -S -o afterburner-wiremock.zip localhost:4757/download/wiremock-gen.1670185260968
    mkdir wiremock/mappings
    unzip afterburner-wiremock.zip -d wiremock/mappings

Now run wiremock with these mappings:

    curl -S -L -o wiremock.jar https://repo1.maven.org/maven2/com/github/tomakehurst/wiremock-jre8-standalone/2.35.0/wiremock-jre8-standalone-2.35.0.jar
    java -jar wiremock.jar --verbose --no-request-journal --port 8844 --root-dir wiremock

Check if wiremock works (in another terminal):

    time curl localhost:8844/delay

Returns:

    {  "durationInMillis" : 0,  "name" : "name",  "message" : "message"}

Re-start afterburner and define the backend to be wiremock:

    java -Dafterburner.remote.call.base_url=http://localhost:8844 -jar target/afterburner-java-*-exec.jar

Now check via afterburner:

    time curl localhost:8080/remote/call\?path=delay

Returns:

    {  "durationInMillis" : 0,  "name" : "name",  "message" : "message"}curl localhost:8080/remote/call  0.01s user 0.01s system 6% cpu 0.290 total

Next, run jmeter script:

    