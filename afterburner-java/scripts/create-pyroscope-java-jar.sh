#!/usr/bin/env bash
set -e

pwd
curl -L -o src/main/jib/pyroscope.jar "https://github.com/grafana/pyroscope-java/releases/download/v0.12.2/pyroscope.jar"

#git clone https://github.com/pyroscope-io/pyroscope-java.git
#TEMP_CLONE_DIR="$(pwd)/pyroscope-java"
#cd pyroscope-java

#function finish() {
#  echo "cleanup"
#  # prevent: Cannot lock Java compile cache ([...]/javaCompile) as it has already been locked by this process.
#  ./gradlew --stop
#  cd ..
#  rm -rf "$TEMP_CLONE_DIR"
#}
#trap finish EXIT
#
#./gradlew shadowJar
#mv agent/build/libs/pyroscope.jar ../../afterburner-java/src/main/jib/

