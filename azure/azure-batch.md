# Azure Batch

Goal: run the JMeter docker on Azure Batch using shipyard to put load on the Afterburner application running on Azure.

Based on: [running-docker-container-on-azure-batch](https://azure.microsoft.com/nl-nl/blog/running-docker-container-on-azure-batch/)

First follow: [batch-shipyard-from-scratch-step-by-step](http://batch-shipyard.readthedocs.io/en/latest/05-batch-shipyard-from-scratch-step-by-step/)

Make sure you have an Azure Batch account and an Azure Storage account in
Azure Portal before continuing with shipyard below. 
Fill the [KEY], [...] placeholders in
`azure/config/credentials.yaml` with the account keys and names as found 
Azure portal.

# Create a jmeter docker and push to registry

First create a docker registry, if not there (added $AFBID number to avoid name clashes, as used for creating the afterburner app):

    ACR_NAME=acrafterburner$AFBID
    az group create --name $ACR_NAME --location westeurope
    az configure -d group=$ACR_NAME location=westeurope
    az acr create --admin-enabled --name $ACR_NAME --sku Basic

Next, push the jmeter image to that registry. Note this uses the docker tag jmeter used in the jmeter docker build process.
    
    DOCKER_REGISTRY=$(az acr show --name $ACR_NAME --query loginServer --output tsv)
    DOCKER_USER=$ACR_NAME
    DOCKER_PASSWORD=$(az acr credential show --name $ACR_NAME --query passwords[0].value --output tsv)
    DOCKER_EMAIL=<your email>
    docker login --username ${DOCKER_USER} --password ${DOCKER_PASSWORD} ${DOCKER_REGISTRY}
    docker tag jmeter ${DOCKER_REGISTRY}/jmeter
    docker push ${DOCKER_REGISTRY}/jmeter
    
Possibly, also push Afterburner if not in the remote Azure docker registry already:
    
    docker tag ${DOCKER_REGISTRY}/afterburner-java
    docker push ${DOCKER_REGISTRY}/afterburner-java

See [instructions](azure-deploy-docker.md) on how to run the
Afterburner image on Azure.

Then, use the jMeter image to deploy to Azure Batch via shipyard:
* point to the jmeter docker image in the config.yaml
* add docker registry plus credentials in credentials.yaml
* create job that runs the jmeter container in jobs.yaml
* supply env parameters to the job in jobs.yaml
* add Azure storage for the jmx file and log files
These steps are detailed below.

# `shipyard` via docker

Follow the steps below to use shipyard via a docker image.
You might want to run via docker to avoid having to install 
the CLI tools locally and run into dependency issues with,
for instance, Python version.

The shipyard docker image can be found here: 
https://hub.docker.com/r/alfpark/batch-shipyard/

To get the latest (notice to get `latest-cli`, the `latest` tag does not exist).

    docker pull alfpark/batch-shipyard:latest-cli

Next, create an alias to run this docker image when calling `shipyard`.

```
    $ alias shipyard="docker run alfpark/batch-shipyard:latest-cli"
    $ shipyard
Usage: shipyard.py [OPTIONS] COMMAND [ARGS]...
```

Include a mapping to a local directory to contain files:

    alias shipyard="docker run --rm -it -v /full/path/to/azure/config:/config -w /config alfpark/batch-shipyard:latest-cli"

See [shipyard usage](https://github.com/Azure/batch-shipyard/blob/master/docs/20-batch-shipyard-usage.md).

Now put the needed yaml files in the local azure directory, e.g. in a `config` directory.
Then run the `pool add` command to add the pool:

    shipyard pool add 

If you need another config dir than /config, you can use this option `--configdir /full/path/to/config`

```
$ shipyard pool add 
2018-06-04 10:24:18.943 INFO - creating container: shipyardgr-stokpopbatch1-mypool
... lot more logging ...
2018-06-04 10:29:18.611 INFO - node tvm-3634815727_1-20180604t102632z: ip XX.XX.XX.XX port 50000
```

Start the job:

    shipyard jobs add --tail stdout.txt

```
2018-06-04 10:32:41.119 INFO - Adding job myjob to pool mypool
2018-06-04 10:32:42.448 DEBUG - submitting 1 tasks (0 -> 0) to job myjob
2018-06-04 10:32:43.120 INFO - submitted all 1 tasks to job myjob
2018-06-04 10:32:43.120 DEBUG - attempting to stream file stdout.txt from job=myjob task=task-00000
26 /etc/group
```

Delete the job:

   shipyard jobs del -y --wait

```
2018-06-04 10:35:17.557 INFO - deleting job: myjob
2018-06-04 10:35:18.252 DEBUG - waiting for job myjob to delete
2018-06-04 10:35:50.020 INFO - job myjob does not exist
```

Delete the pool:

   shipyard pool del -y

```
2018-06-04 10:37:59.369 INFO - Deleting pool: mypool
... more logging ...
2018-06-04 10:38:00.077 DEBUG - deleting table: shipyardregistry
```

Done! Check if all is gone:

    shipyard pool list 

```
2018-06-04 10:40:46.823 INFO - list of pools
* pool id: mypool
... more logging ...
  * node agent: batch.node.ubuntu 16.04
```

Not yet deleted... but some time later:

    shipyard pool list 

```
2018-06-04 10:42:42.802 ERROR - no pools found
```

# Add Azure storage and upload file

First, create a storage account and a jmeter share to store the jmeter jmx files and
other load script related files, such as properties and csv files. Also the log and jtl 
files will be stored there after the test.

See: [storage-how-to-use-files-cli](https://docs.microsoft.com/en-us/azure/storage/files/storage-how-to-use-files-cli).

```
RESOURCE_GROUP_NAME=grpafterburner$AFBID
STORAGENAME=afterburnerstorage

STORAGEACCT=$(az storage account create \
--resource-group $RESOURCE_GROUP_NAME \
--name "$STORAGENAME" \
--location westeurope \
--sku Standard_LRS \
--query "name" | tr -d '"')

STORAGEKEY=$(az storage account keys list \
--resource-group $RESOURCE_GROUP_NAME \
--account-name $STORAGEACCT \
--query "[0].value" | tr -d '"')

az storage share create \
--account-name $STORAGEACCT \
--account-key $STORAGEKEY \
--name "jmeter"
```

Upload the jmeter files into a unique name for a job run (e.g. run00001).

```
az storage directory create \
--account-name $STORAGEACCT \
--account-key $STORAGEKEY \
--share-name "jmeter" \
--name "run00001"

az storage file upload \
--account-name $STORAGEACCT \
--account-key $STORAGEKEY \
--share-name "jmeter" \
--source "~/git/afterburner/afterburner-loadtest-jmeter/afterburner-simple.jmx" \
--path "run00001/afterburner-simple.jmx"
```

After the test list the files and download what is needed to process further:

```
az storage file list \
--account-name $STORAGEACCT \
--account-key $STORAGEKEY \
--share-name "jmeter" \
--path "run00001" \
--output table

az storage file download \
--account-name $STORAGEACCT \
--account-key $STORAGEKEY \
--share-name "jmeter" \
--path "run00001/jmeter.log" \
--dest "~/jmeter.log"

az storage file download \
--account-name $STORAGEACCT \
--account-key $STORAGEKEY \
--share-name "jmeter" \
--path "run00001/result.jtl" \
--dest "~/result.jtl"
```

Delete account after usage (be careful, will delete all files):

```
az storage account delete \
    --resource-group $RESOURCE_GROUP_NAME \
    --name $STORAGEACCT \
    --yes
```

# Add mount to Azure storage

To run a jMeter script and store the logging a mount to Azure Storage
is added to the job. The job is a docker jMeter instance. 

Basically
run the following `jobs add` shipyard command with proper config files.

     shipyard jobs add --tail stdout.txt

The important parts in the config files are:

### config.yaml

```
batch_shipyard:
  storage_account_settings: afterburnerstorage
global_resources:
  docker_images:
  - acrafterburner01.azurecr.io/jmeter
  volumes:
    shared_data_volumes:
      azurefile_vol:
        volume_driver: azurefile
        storage_account_settings: afterburnerstorage
        azure_file_share_name: jmeter
        container_path: $AZ_BATCH_NODE_SHARED_DIR/azfile
        mount_options:
        - file_mode=0777
        - dir_mode=0777
        bind_options: rw
```

### pool.yaml

```
  input_data:
    azure_storage:
    - storage_account_settings: afterburnerstorage
      remote_path: jmeter
      local_path: $AZ_BATCH_NODE_SHARED_DIR/jmeter
```

### jobs.yaml

To run the jmeter script with the correct files:

```
    docker_image: acrafterburner01.azurecr.io/jmeter
    -Jjmx.domain=$JMETER_JMX_DOMAIN -n -t $JMETER_FILE_DIR/$JMETER_SCRIPT_NAME -l $JMETER_FILE_DIR/result.jtl -j $JMETER_FILE_DIR/jmeter.log
```

### credentials.yaml

Fill with the needed keys.

# References

* The shipyard docker image: https://hub.docker.com/r/alfpark/batch-shipyard/
* Shipyard docs: https://github.com/Azure/batch-shipyard/tree/master/docs
* Some hints on volume mappings: https://github.com/AdamPaternostro/Azure-Docker-Shipyard

# Troubleshooting

Use a simple busybox docker image to do simple testing, such as figuring out
which mounts are available or which values env vars have inside the started
docker job. E.g.:

Use busybox from jobs.yaml:

    docker_image: <docker registry>/busybox 
    command: ls -alR /mnt

or

    docker_image: <docker registry>/busybox 
    command: env

Get more (debug) logging when starting a job by supplying -v 
and possibly tail stderr.txt instead of stdout.txt, like so:

    shipyard jobs add -v --tail stderr.txt

This show valuable info such as the complete command used to start docker:

```
2018-06-06 10:31:36.304 DEBUG convoy.batch:_construct_task:4345 task: task-00007 command: /bin/bash -c 'set -e; set -o pipefail; [ -f .shipyard.envlist ] && export $(cat .shipyard.envlist | xargs); env | grep AZ_BATCH_ >> .shipyard.envlist; docker run --rm --name myjob1-task-00007 -v $AZ_BATCH_NODE_ROOT_DIR:$AZ_BATCH_NODE_ROOT_DIR -w $AZ_BATCH_TASK_WORKING_DIR --env-file .shipyard.envlist busybox ls -al /jmeter; wait'
```
    
# Cleanup

Delete batch account:

```
az batch batch account delete \
    --resource-group $RESOURCE_GROUP_NAME \
    --name $ \
    --yes
```
