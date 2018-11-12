#!/usr/bin/env bash

echo "Delete all created resources in azure"

az batch account delete \
    --resource-group $RESOURCE_GROUP \
    --name $BATCH_ACCOUNT_NAME \
    --yes --no-wait
    --yes --no-wait

az webapp delete \
    --name $APP_NAME \
    --resource-group $RESOURCE_GROUP

az appservice plan delete \
  --name $APP_SERVICE_NAME \
  --resource-group $RESOURCE_GROUP \
  --yes

az acr delete \
  --name $REGISTRY_NAME \
  --resource-group $RESOURCE_GROUP

az vm delete \
  --name $VM_JMETER_NAME \
  --resource-group $RESOURCE_GROUP \
  --yes --no-wait

echo "Remove generated files"
rm -v config/credentials.yaml
rm -v config/pool.yaml
rm -v config/jobs.yaml
rm -v config/config.yaml

