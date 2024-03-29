# Afterburner with jMeter

# maven

To run with maven:

    $ mvn clean verify

# docker

Run the afterburner jMeter script in a docker instance.

This is based on this article: [make-use-of-docker-with-jmeter-learn-how](https://www.blazemeter.com/blog/make-use-of-docker-with-jmeter-learn-how)

# instructions

Build the docker from this directory:
    
    $ docker build --file docker/Dockerfile --tag jmeter docker
    
Export path (folder) to where your `afterburner-simple.jmx` file lives. The APP_NAME environment variable is from the run 
Afterburner in docker step-by-step guide.

    $ export JMETER_FILES_PATH=<path>
    $ export JMETER_SCRIPT_NAME=afterburner-simple.jmx
    $ export JMETER_JMX_DOMAIN=$APP_NAME.azurewebsites.net

Run the jmeter docker:

    $ run-docker.sh
    
