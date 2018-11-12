#!/usr/bin/env bash

if [ -z "$AFBID" ] || [ -z "$RESOURCE_GROUP" ]; then
    echo "Please provide AFBID first: export AFBID=<number>"
    echo "Please provide RESOURCE_GROUP first: export RESOURCE_GROUP=<rg_id>"
else
    export APP_SERVIVE_NAME=linuxappservice$AFBID
    export BATCH_ACCOUNT_NAME=batchaccount$AFBID
    export STORAGE_NAME=jmeterstorage$AFBID
    export JMETER_SHARE=jmetershare$AFBID 
    export REGISTRY_NAME=acrafterburner$AFBID
    export DOCKER_REGISTRY=$(az acr show --name $REGISTRY_NAME --query loginServer --output tsv)
    export DOCKER_USER=$REGISTRY_NAME
    export DOCKER_PASSWORD=$(az acr credential show --name $REGISTRY_NAME --query passwords[0].value --output tsv)
    export DOCKER_EMAIL=$MY_EMAIL
    export STORAGE_KEY=$(az storage account keys list \
    --resource-group $RESOURCE_GROUP \
    --account-name $STORAGE_NAME \
    --query "[0].value" | tr -d '"')
    export APP_NAME=appafterburner$AFBID
    export AFTERBURNER_URL=$(az webapp show --name $APP_NAME --query hostNames[0] --out tsv)

    export BATCH_ACCOUNT_KEY=$(az batch account keys list \
    --resource-group $RESOURCE_GROUP \
    --name $BATCH_ACCOUNT_NAME \
    --query "primary" | tr -d '"')
fi
