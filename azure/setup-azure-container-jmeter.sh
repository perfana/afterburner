#!/usr/bin/env bash

JMETER_FILES_MOUNT=/mnt/jmeter
JMETER_FILES_PATH=$JMETER_FILES_MOUNT/run00001/
JMETER_SCRIPT_NAME=afterburner-simple.jmx
JMETER_JMX_DOMAIN=$AFTERBURNER_URL
JMETER_CONTAINER_NAME=jmetercontainer$AFBID 

## to use virtual network add following parameters
#JMETER_CONTAINTER_VNET_NAME=[VNET_NAME]
#JMETER_CONTAINER_SUBNET=[SUBNET]
#  --subnet $JMETER_CONTAINTER_VNET_NAME \
#  --subnet $JMETER_CONTAINER_SUBNET \

az container create \
  --name $JMETER_CONTAINER_NAME \
  --resource-group $RESOURCE_GROUP \
  --registry-login-server $DOCKER_REGISTRY \
  --registry-username $DOCKER_USER \
  --registry-password $DOCKER_PASSWORD \
  --azure-file-volume-account-key $STORAGE_KEY \
  --azure-file-volume-account-name $STORAGE_NAME \
  --azure-file-volume-mount-path $JMETER_FILES_MOUNT \
  --azure-file-volume-share-name $JMETER_SHARE \
  --image $DOCKER_REGISTRY/jmeter \
  --command-line "jmeter -n -Jjmx.domain=$JMETER_JMX_DOMAIN -t $JMETER_FILES_PATH/afterburner-simple.jmx -l $JMETER_FILES_PATH/tmp/result.jtl -j $JMETER_FILES_PATH/tmp/jmeter.log"

