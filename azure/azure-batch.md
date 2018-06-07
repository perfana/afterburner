# Azure Batch

Now we should be able to run the JMeter docker on Azure Batch using shipyard.

Based on: [running-docker-container-on-azure-batch](https://azure.microsoft.com/nl-nl/blog/running-docker-container-on-azure-batch/)

First follow: [batch-shipyard-from-scratch-step-by-step](http://batch-shipyard.readthedocs.io/en/latest/05-batch-shipyard-from-scratch-step-by-step/)

# Create a jmeter docker and push to registry

First create a docker registry, if not there:

    az group create --name afterburner01 --location westeurope
    az configure -d group=afterburner01 location=westeurope
    az acr create --admin-enabled --name acrafterburner01 --sku Basic

Next, push the jmeter image to that registry:
    
    export DOCKER_REGISTRY=$(az acr show --name acrafterburner01 --query loginServer --output tsv)
    export DOCKER_USER=acrafterburner01
    export DOCKER_PASSWORD=$(az acr credential show --name acrafterburner01 --query passwords[0].value --output tsv)
    export DOCKER_EMAIL=<your email>
    docker login --username ${DOCKER_USER} --password ${DOCKER_PASSWORD} ${DOCKER_REGISTRY}
    docker tag <your hash for jmeter image> ${DOCKER_REGISTRY}/jmeter
    docker push ${DOCKER_REGISTRY}/jmeter
    
Possibly also push afterburner:
    
    docker push ${DOCKER_REGISTRY}/afterburner-java

Then, use that image to deploy to Azure Batch via shipyard:

TODO; add extended info for steps below

Edit config/ files to 
* point to the jmeter docker image
* add docker registry plus credentials
* create job that runs the jmeter container
* supply env parameters to the job
* add storage for the jmx file and log files




    
    
