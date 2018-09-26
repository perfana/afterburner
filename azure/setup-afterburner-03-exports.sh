#!/usr/bin/env bash
export DOCKER_REGISTRY=$(az acr show --name $REGISTRY_NAME --query loginServer --output tsv)
export DOCKER_USER=$REGISTRY_NAME
export DOCKER_PASSWORD=$(az acr credential show --name $REGISTRY_NAME --query passwords[0].value --output tsv)
export DOCKER_EMAIL=$MY_EMAIL
printenv | grep DOCKER
