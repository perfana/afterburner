#!/usr/bin/env bash

echo "Create new cloud-init.txt"
eval "cat <<EOF
$(<cloud-init.template.txt)
EOF
" > cloud-init.txt 2> /dev/null

echo "Create jmeter runner vm"
az vm create \
  --resource-group $RESOURCE_GROUP \
  --name $VM_JMETER_NAME \
  --image UbuntuLTS \
  --admin-username $VM_ADMIN_USER \
  --admin-password $VM_ADMIN_PASSWORD \
  --public-ip-address "" \
  --subnet $VNET_SUBNET \
  --boot-diagnostics-storage $STORAGE_NAME \
  --custom-data cloud-init.txt
