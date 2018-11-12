# Afterburner on Azure

## quick start

There are helper scripts to get you quickly up and running. 

This command will run all of the setup scripts. Tip: comment out lines you do not want to execute.

    setup.sh

Before running setup.sh make the following environment variables available to the setup script:

    export MY_EMAIL=[email]
    export RESOURCE_GROUP=[existing azure resource group]

If you have no existing resource group, you can give a non existing resource group name and
execute 'azure/setup-resource-group.sh'.

Also the following commands should be on your PATH: java, docker, az

Make sure you have run 'az login'.

Beware: do not source the files below directly, they might exit and close your terminal session.

This command makes afterburner available in azure:

    source setup-afterburner.sh 

This command makes a load test docker in the azure batch registry:

    source setup-loadtest-jmeter.sh

This command makes azure batch jmeter runner available:

    source setup-azure-batch-jmeter.sh

* [Run Afterburner docker image on Azure](azure-deploy-docker.md)
* [Run jMeter docker image on Azure Batch](azure-batch.md)
