#!/usr/bin/env bash
set -e

# download jars to be used at runtime and package them into the image

if [ ! -f src/main/jib/pyroscope.jar ]; then
  echo "Downloading pyroscope jar"
  curl -Ss -L -o src/main/jib/pyroscope.jar "https://github.com/grafana/pyroscope-java/releases/download/v0.13.0/pyroscope.jar"
else
  echo "Pyroscope jar already exists"
fi

if [ ! -f src/main/jib/jfr-exporter.jar ]; then
  echo "Downloading JFR exporter jar"
  curl -Ss -L -o src/main/jib/jfr-exporter.jar "https://github.com/perfana/jfr-exporter/releases/download/0.3.0/jfr-exporter-0.3.0.jar"
else
  echo "JFR exporter jar already exists"
fi

