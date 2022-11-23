#!/usr/bin/env bash
set -e
wget -O dd-java-agent.jar 'https://dtdg.co/latest-java-tracer' 

mv dd-java-agent.jar ../afterburner-java/src/main/jib/

