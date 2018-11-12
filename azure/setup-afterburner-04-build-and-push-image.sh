#!/usr/bin/env bash
cd ../afterburner-java/
../mvnw clean install -DskipTests
../mvnw package docker:build -DpushImage
cd - &>/dev/null
