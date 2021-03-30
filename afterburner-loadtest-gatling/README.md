# Load tests for afterburner

This directory contains load tests for afterburner.

## afterburner-gatling

### Execute

Either run the `Engine` scala class or use maven from the `afterburner-loadtest-gatling` directory:

    mvn events-gatling:test
     
### Creation

These tests are created using:

    mvn org.apache.maven.plugins:maven-archetype-plugin:2.4:generate --batch-mode \
     -DarchetypeGroupId=io.gatling.highcharts \
     -DarchetypeArtifactId=gatling-highcharts-maven-archetype \
     -DarchetypeVersion=2.3.1 \
     -DgroupId=nl.stokpop \
     -DartifactId=afterburner-loadtest \
     -Dversion=1.0-SNAPSHOT \
     -Dpackage=nl.stokpop

### Updates after creation

#### maven-archetype-plugin
`maven-archetype-plugin` version 3.0.1 does not seem to create the files, so switched to version 2.4.

#### maven-gatling-plugin
Added `maven-gatling-plugin` version 2.2.4. Got the following error:

    Caused by: java.lang.NoClassDefFoundError: scala/reflect/internal/SymbolTable$ReflectStats
     
Fix: also needed to add `scala-reflect` dependency.    

#### scala.version
Moved to scala version 2.12.4 to 2.12.5.

