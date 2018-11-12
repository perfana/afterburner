#!/usr/bin/env bash
cd ../afterburner-loadtest-jmeter/
docker build --file docker/Dockerfile --tag jmeter docker
cd - &>/dev/null

