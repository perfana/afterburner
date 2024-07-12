#!/usr/bin/env bash
set -e

# download jars to be used at runtime and package them into the image

if [ ! -f src/main/jib/pyroscope.jar ]; then
  PYROSCOPE_VERSION="0.14.0"
  echo "Downloading pyroscope jar version $PYROSCOPE_VERSION"
  curl -Ss -L -o src/main/jib/pyroscope.jar "https://github.com/grafana/pyroscope-java/releases/download/v${PYROSCOPE_VERSION}/pyroscope.jar"
else
  echo "Pyroscope jar already exists"
fi

if [ ! -f src/main/jib/jfr-exporter.jar ]; then
  JFR_EXPORTER_VERSION=0.5.0
  echo "Downloading JFR exporter jar version $JFR_EXPORTER_VERSION"
  curl -Ss -L -o src/main/jib/jfr-exporter.jar "https://github.com/perfana/jfr-exporter/releases/download/${JFR_EXPORTER_VERSION}/jfr-exporter-${JFR_EXPORTER_VERSION}.jar"
else
  echo "JFR exporter jar already exists"
fi

