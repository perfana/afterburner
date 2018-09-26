#!/usr/bin/env bash
az appservice plan create --name linuxappservice --is-linux --sku S1
az webapp create --name $APP_NAME --plan linuxappservice --deployment-container-image-name https://$REGISTRY_NAME.azurecr.io/afterburner-java
az webapp config container set --name $APP_NAME \
    --docker-custom-image-name ${DOCKER_REGISTRY}/afterburner-java:latest \
    --docker-registry-server-url https://${DOCKER_REGISTRY} \
    --docker-registry-server-password ${DOCKER_PASSWORD} \
    --docker-registry-server-user ${DOCKER_USER}
az webapp config appsettings set --settings PORT=8080 --name $APP_NAME
az webapp restart --name $APP_NAME
export AFTERBURNER_URL=$(az webapp show --name $APP_NAME --query hostNames[0] --out tsv)
printenv | grep AFTERBURNER_URL
curl ${AFTERBURNER_URL}/delay