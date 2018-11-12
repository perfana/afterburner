#!/usr/bin/env bash
source setup-afterburner-06-create-image.sh
docker login --username ${DOCKER_USER} --password ${DOCKER_PASSWORD} ${DOCKER_REGISTRY}
docker tag jmeter ${DOCKER_REGISTRY}/jmeter
docker push ${DOCKER_REGISTRY}/jmeter
