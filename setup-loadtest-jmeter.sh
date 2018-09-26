#!/usr/bin/env bash
cd afterburner-loadtest-jmeter
source setup-afterburner-06-create-image.sh
cd - >/dev/null
docker login --username ${DOCKER_USER} --password ${DOCKER_PASSWORD} ${DOCKER_REGISTRY}
docker tag jmeter ${DOCKER_REGISTRY}/jmeter
docker push ${DOCKER_REGISTRY}/jmeter
