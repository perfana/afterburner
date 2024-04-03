#!/usr/bin/env bash
set -e

# download jars to be used at runtime and package them into the image

if [ -f src/main/jib/pyroscope.jar ]; then
  echo "Remove pyroscope jar"
  rm -v src/main/jib/pyroscope.jar
else
  echo "No Pyroscope jar to remove"
fi

if [ -f src/main/jib/jfr-exporter.jar ]; then
  echo "Remove JFR exporter jar"
  rm -v src/main/jib/jfr-exporter.jar
else
  echo "No JFR exporter jar to remove"
fi

