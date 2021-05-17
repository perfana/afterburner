#!/usr/bin/env bash

BYTEMAN_URL="https://downloads.jboss.org/byteman/4.0.14/byteman-download-4.0.14-bin.zip"
BYTEMAN_ZIP=$(basename $BYTEMAN_URL)

MAIN_DIR=$( echo $BYTEMAN_ZIP | sed "s/^\(.*\)-bin.zip$/\1/" )

cd byteman || (echo "byteman dir not found!"; exit 2)
echo "download $BYTEMAN_URL"
curl -Ss -O -L $BYTEMAN_URL
echo "unzip $BYTEMAN_ZIP"
unzip $BYTEMAN_ZIP
rm -v $BYTEMAN_ZIP
mv -v $MAIN_DIR btm

