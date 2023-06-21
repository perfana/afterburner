# Load tests for afterburner

This directory contains load tests for afterburner.

## afterburner-gatling

### Execute

    ../../mvnw events-gatling:test
     
### Use Perfana API key

Create a Perfana API key in the Perfana gui, and make available to the load script via:

    export PERFANA_API_KEY=$(pbpaste)

This will paste the copy buffer into the variable. You can also use the key directly. 

### Use g2i tool

The load script will download the g2i tool automatically. This is a version for linux.
If you need to run on another platform, e.g. macOS, build g2i yourself and put in this directory.

See: https://github.com/perfana/gatling-to-influxdb 


