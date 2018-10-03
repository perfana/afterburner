#!/usr/bin/env bash
export BATCH_ACCOUNT_NAME=batchaccount$AFBID
export STORAGE_NAME=jmeterstorage$AFBID
export JMETER_SHARE=jmetershare$AFBID

az batch account create \
  --resource-group $RESOURCE_GROUP \
  --name $BATCH_ACCOUNT_NAME \
  --location westeurope

az storage account create \
--resource-group $RESOURCE_GROUP \
--name $STORAGE_NAME \
--location westeurope \
--sku Standard_LRS

# Attach Storage account to Batch account
az batch account set \
    --resource-group $RESOURCE_GROUP \
    --name $BATCH_ACCOUNT_NAME \
    --storage-account $STORAGE_NAME

export BATCH_ACCOUNT_KEY=$(az batch account keys list \
--resource-group $RESOURCE_GROUP \
--name $BATCH_ACCOUNT_NAME \
--query "primary" | tr -d '"')

export STORAGE_KEY=$(az storage account keys list \
--resource-group $RESOURCE_GROUP \
--account-name $STORAGE_NAME \
--query "[0].value" | tr -d '"')

az storage share create \
--account-name $STORAGE_NAME \
--account-key $STORAGE_KEY \
--name $JMETER_SHARE

source replace-shipyard-config.sh

docker pull alfpark/batch-shipyard:latest-cli
CONFIG_PATH=$(pwd)/config
SHIPYARD="docker run --rm -it -v $CONFIG_PATH:/config -w /config alfpark/batch-shipyard:latest-cli"

$SHIPYARD pool add 

az storage directory create \
--account-name $STORAGE_NAME \
--account-key $STORAGE_KEY \
--share-name $JMETER_SHARE \
--name "run00001"

az storage file upload \
--account-name $STORAGE_NAME \
--account-key $STORAGE_KEY \
--share-name $JMETER_SHARE \
--source "../afterburner-loadtest-jmeter/afterburner-simple.jmx" \
--path "run00001/afterburner-simple.jmx"

$SHIPYARD jobs add --tail stdout.txt

az storage file list \
--account-name $STORAGE_NAME \
--account-key $STORAGE_KEY \
--share-name $JMETER_SHARE \
--path "run00001/tmp" \
--output table

az storage file download \
--account-name $STORAGE_NAME \
--account-key $STORAGE_KEY \
--share-name $JMETER_SHARE \
--path "run00001/tmp/jmeter.log" \
--dest "~/jmeter.log"

az storage file download \
--account-name $STORAGE_NAME \
--account-key $STORAGE_KEY \
--share-name $JMETER_SHARE \
--path "run00001/tmp/result.jtl" \
--dest "~/result.jtl"

