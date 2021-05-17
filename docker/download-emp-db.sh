#!/usr/bin/env bash

EMP_DB_URL="https://github.com/datacharmer/test_db/releases/download/v1.0.7/test_db-1.0.7.tar.gz"
EMP_DB=$(basename $EMP_DB_URL)

echo "download $EMP_DB_URL"
curl -Ss -O -L $EMP_DB_URL
echo "untar $EMP_DB"
tar xfz "$EMP_DB"
mv -v test_db employee-db/
rm -v "$EMP_DB"