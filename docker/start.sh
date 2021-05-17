#!/bin/bash

if [ ! -f employee-db/test_db/employees.sql ]; then
    echo "First download and unpack employees db"
    ./download-emp-db.sh
fi

export COMPOSE_PROJECT_NAME=workshop

# only sleep when image does not exit yet, need time to load database
SLEEP=60
MARIADB_DOCKER_NAME="${COMPOSE_PROJECT_NAME}_mariadb_1"
[[ "$(docker ps)" =~ $MARIADB_DOCKER_NAME ]] && SLEEP=0

docker compose up -d mariadb
if [ $SLEEP -ne 0 ]; then
    echo "sleep for $SLEEP seconds for employee db to load"
    sleep $SLEEP
fi
docker compose up -d afterburner
docker compose up -d wrk2
