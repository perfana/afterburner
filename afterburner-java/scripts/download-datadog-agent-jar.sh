#!/usr/bin/env bash
set -e

curl https://dtdg.co/latest-java-tracer --location --output dd-java-agent.jar

mv dd-java-agent.jar ../afterburner-java/src/main/jib/

