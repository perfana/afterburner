# Afterburner in Docker on Azure 

Based on this [deploy-a-spring-boot-app-to-azure](https://azure.microsoft.com/en-us/resources/videos/deploy-a-spring-boot-app-to-azure/) video. 

Commands used on OSX:

First install Azure CLI:

    $ brew update && brew install azure-cli

Then login to your Azure account:

    $ az login

Create a group in your region (use some other name for `afterburner01`):

    $ az group create --name afterburner01 --location westeurope

Configure the group and location, so it is default for the commands that follow:

    $ az configure -d group=afterburner01 location=westeurope
    
Create a docker registry to push the docker image to (use some other name for `acrafterburner01`):

    $ az acr create --admin-enabled --name acrafterburner01 --sku Basic

Export all docker access information in environment variables, use your own `your@email`:

    $ export DOCKER_REGISTRY=$(az acr show --name acrafterburner01 --query loginServer --output tsv)
    $ export DOCKER_USER=acrafterburner01
    $ export DOCKER_PASSWORD=$(az acr credential show --name acrafterburner01 --query passwords[0].value --output tsv)
    $ export DOCKER_EMAIL=your@email

In mvn settings.xml add registry server information:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" 
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
        xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">

    <servers>
        <server>
            <id>${env.DOCKER_REGISTRY}</id>
            <username>${env.DOCKER_USER}</username>
            <password>${env.DOCKER_PASSWORD}</password>
            <configuration>
                <email>${env.DOCKER_EMAIL}</email>
            </configuration>
        </server>
    </servers>
</settings>
```

in top level pom.xml:

```xml
<properties>
    <docker.image.prefix>${env.DOCKER_REGISTRY}</docker.image.prefix>
    <docker.registry>${env.DOCKER_REGISTRY}</docker.registry>
</properties>
```

in pom.xml:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.spotify</groupId>
            <artifactId>docker-maven-plugin</artifactId>
            <version>0.4.14</version>
            <configuration>
                <imageName>${docker.image.prefix}/${project.artifactId}</imageName>
                <dockerDirectory>src/main/docker</dockerDirectory>
                <buildArgs>
                    <JAR_FILE>${project.build.finalName}.jar</JAR_FILE>
                </buildArgs>
                <serverId>${docker.registry}</serverId>
                <registryUrl>https://${docker.registry}</registryUrl>
            </configuration>
        </plugin>
    </plugins>
</build>
``` 

And add `spring-boot-maven-plugin`:

```xml
    <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <version>${springboot.version}</version>
        <configuration>
            <mainClass>nl.stokpop.afterburner.Afterburner</mainClass>
        </configuration>
        <executions>
            <execution>
                <goals>
                    <goal>repackage</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
```

Create a docker directory:

    $ mkdir src/main/docker

Create Dockerfile in src/main/docker/:

```
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} afterburner-springboot.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/afterburner-springboot.jar"]
```

Build and push image to Azure registry: 

    $ mvn package docker:build -DpushImage

Check the image:

    $ az acr repository list --name acrafterburner01

Create service plan for to run docker on linux:

    $ az appservice plan create --name linuxappservice --is-linux --sku S1

Create webapp with this service plan and the afterburner container:

    $ az webapp create --name afterburner01 --plan linuxappservice --deployment-container-image-name https://acrafterburner01.azurecr.io/afterburner-java

Set config for pulling the container from the registry:

    $ az webapp config container set --name afterburner01 \
        --docker-custom-image-name ${DOCKER_REGISTRY}/afterburner-java:latest \
        --docker-registry-server-url https://${DOCKER_REGISTRY} \
        --docker-registry-server-password ${DOCKER_PASSWORD} \
        --docker-registry-server-user ${DOCKER_USER}

Set internal port inside container:

    $ az webapp config appsettings set --settings PORT=8080 --name afterburner01

Start/restart afterburner:

    $ az webapp restart --name afterburner01

Query the url for the afterburner application:

    $ export AFTERBURNER_URL=$(az webapp show --name afterburner01 --query hostNames[0] --out tsv)
    
Check if it works:

    $ curl ${AFTERBURNER_URL}/delay

# Troubleshoot

If you get this error:

```
[WARNING] Failed to push acrafterburner01.azurecr.io/afterburner-java, retrying in 10 seconds (5/5).
[INFO] Pushing acrafterburner01.azurecr.io/afterburner-java
The push refers to repository [acrafterburner01.azurecr.io/afterburner-java]
```

If container does not start correctly (`503 Service Temporarily Unavailable`), 
try to run the container locally to see if it works:

```
$ curl -v afterburner01.azurewebsites.net
* Rebuilt URL to: afterburner01.azurewebsites.net/
*   Trying XXX.XXX.XXX.XXX...
* TCP_NODELAY set
* Connected to afterburner01.azurewebsites.net (XXX.XXX.XXX.XXX) port 80 (#0)
> GET / HTTP/1.1
> Host: afterburner01.azurewebsites.net
> User-Agent: curl/7.54.0
> Accept: */*
>
< HTTP/1.1 503 Service Temporarily Unavailable
< Content-Length: 19
< Server: nginx
< Set-Cookie: ARRAffinity=XYZ;Path=/;HttpOnly;Domain=afterburner01.azurewebsites.net
< Date: Mon, 30 Apr 2018 15:45:35 GMT
<
* Connection #0 to host afterburner01.azurewebsites.net left intact
Service Unavailable
```
Check if `settings.xml` contains the correct server definition for the registry.
And check if all environment variables set.


Login to the remote docker registry:
    
    $ docker login --username ${DOCKER_USER} --password ${DOCKER_PASSWORD} ${DOCKER_REGISTRY}

Pull the latest image:

    $ docker pull ${DOCKER_REGISTRY}/afterburner-java:latest

Check if it runs:

    $ docker run -i -t ${DOCKER_REGISTRY}/afterburner-java
    Error: Invalid or corrupt jarfile /app.jar

Get to the shell of your docker to check out the image status:

```
$ docker run -it --entrypoint sh ${DOCKER_REGISTRY}/afterburner-java:latest
/ # ls
app.jar  bin      dev      etc      home     lib      media    mnt      proc     root     run      sbin     srv      sys      tmp      usr      var
/ # ls -al
total 64
drwxr-xr-x    1 root     root          4096 Apr 30 18:31 .
drwxr-xr-x    1 root     root          4096 Apr 30 18:31 ..
-rwxr-xr-x    1 root     root             0 Apr 30 18:31 .dockerenv
drwxr-xr-x    2 root     root          4096 Apr 30 18:15 app.jar
...
```

In this example the `app.jar` was a directory instead of a proper file. 
The Spotify `docker-maven-plugin` was not configured correctly. 

Another issue: the app.jar was too small. Only the slim `app.jar` was deployed, not the full blown self running jar, because the Springboot maven repackage plugin was not executed in a maven build.
This was solved by adding the repackage goal:

```xml
    <executions>
        <execution>
            <goals>
                <goal>repackage</goal>
            </goals>
        </execution>
    </executions>
```
Show the history of the image:
 
    $ docker image history --no-trunc ${DOCKER_REGISTRY}/afterburner-java:latest > image_history

Get into a docker shell and expose port 8080 so you can try running the app.jar manually:

    $ docker run -it -p 8080:8080 --entrypoint sh ${DOCKER_REGISTRY}/afterburner-java:latest
