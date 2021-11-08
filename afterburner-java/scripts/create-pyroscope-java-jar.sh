#!/usr/bin/env bash
set -e
rm -rf pyroscope-java/
git clone https://github.com/pyroscope-io/pyroscope-java.git
cd pyroscope-java
./gradlew shadowJar
cp agent/build/libs/pyroscope.jar ../../afterburner-java/src/main/jib
cd ..
rm -rf pyroscope-java/
