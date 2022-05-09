#!/usr/bin/env bash

# fail on error
set -e

TMDA_URL="https://public.dhe.ibm.com/software/websphere/appserv/support/tools/jca/jca4611.jar"
TMDA_JAR=$(basename $TMDA_URL)

echo "download $TMDA_URL"
curl -O -L $TMDA_URL
echo "to start: java -jar $TMDA_JAR"

