#!/usr/bin/env bash
az acr create --admin-enabled --name $REGISTRY_NAME --sku Basic
