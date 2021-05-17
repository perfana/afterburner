#!/usr/bin/env bash

echo "LOAD employee db"
cd /docker-entrypoint-initdb.d/test_db || (echo "ERROR: cannot find test_db dir" && exit)
mysql -t -uroot -p$MYSQL_ROOT_PASSWORD < employees.sql