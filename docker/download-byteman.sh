#!/usr/bin/env bash

# fail on error
set -e

BYTEMAN_URL="https://downloads.jboss.org/byteman/4.0.18/byteman-download-4.0.18-bin.zip"
BYTEMAN_ZIP=$(basename $BYTEMAN_URL)

MAIN_DIR=$( echo $BYTEMAN_ZIP | sed "s/^\(.*\)-bin.zip$/\1/" )

cd byteman || (echo "byteman dir not found!"; exit 2)
echo "download $BYTEMAN_URL"
curl -O -L $BYTEMAN_URL
echo "unzip $BYTEMAN_ZIP"
unzip $BYTEMAN_ZIP
rm -v $BYTEMAN_ZIP
mv -v $MAIN_DIR btm

