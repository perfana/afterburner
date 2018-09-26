#!/usr/bin/env bash
../mvnw clean install -DskipTests
../mvnw package docker:build -DpushImage
