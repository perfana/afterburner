#!/bin/sh

java $JAVA_OPTS \
  -cp /app/resources:/app/classes:/app/libs/* \
  io.perfana.afterburner.Afterburner