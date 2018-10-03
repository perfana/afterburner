#!/usr/bin/env bash

[ -z "$MY_EMAIL" ] \
    && echo "first 'export MY_EMAIL=[EMAIL]'" \
    && exit 1
[ -z "$RESOURCE_GROUP" ] \
    && echo "first 'export RESOURCE_GROUP=[RESOURCE_GROUP]'" \
    && echo "and optionally run 'setup-resource-group.sh' if non-existing resource group" \
    && exit 1

export AFBID=${AFBID:-$RANDOM}
export REGISTRY_NAME=acrafterburner$AFBID
export APP_NAME=appafterburner$AFBID
printenv | grep $AFBID
