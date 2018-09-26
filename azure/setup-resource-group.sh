#!/usr/bin/env bash
export RESOURCE_GROUP=rgafterburner
az group create --name $RESOURCE_GROUP --location westeurope
az configure -d group=$RESOURCE_GROUP location=westeurope
