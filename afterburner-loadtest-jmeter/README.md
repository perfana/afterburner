# Afterburner with jMeter

# docker

Run the afterburner jMeter script in a docker instance.

This is based on this article: [make-use-of-docker-with-jmeter-learn-how](https://www.blazemeter.com/blog/make-use-of-docker-with-jmeter-learn-how)

# instructions

Build the docker form `docker` directory:
    
    $ docker build -t jmeter .
    
Export path to your `afterburner-simple.jmx` file:

    $ export JMETER_FILES_PATH=<path>
    $ export JMETER_SCRIPT_NAME=afterburner-simple.jmx

Run the jmeter docker:

    $ run-docker.sh
    

