#!/bin/bash

java \
-Dcom.sun.management.jmxremote \
-Dcom.sun.management.jmxremote.port=5000 \
-Dcom.sun.management.jmxremote.rmi.port=5000 \
-Dcom.sun.management.jmxremote.local.only=false \
-Dcom.sun.management.jmxremote.authenticate=false \
-Dcom.sun.management.jmxremote.ssl=false \
-jar target/afterburner-java-1.1-SNAPSHOT-exec.jar

