#!/bin/bash

mkdir -p -v dumps
mkdir -p -v byteman
mkdir -p -v mariadb

if [ ! -f employee-db/test_db/employees.sql ]; then
    echo "First download and unpack employees db"
    ./download-emp-db.sh
fi

export COMPOSE_PROJECT_NAME=workshop

# only sleep when image does not exit yet, need time to load database
SLEEP=30
MARIADB_DOCKER_NAME="${COMPOSE_PROJECT_NAME}-mariadb-1"
[[ "$(docker ps)" =~ $MARIADB_DOCKER_NAME ]] && SLEEP=0

docker compose up -d mariadb
if [ $SLEEP -ne 0 ]; then
    echo "sleep for $SLEEP seconds for employee db to load"
    sleep $SLEEP
fi
docker compose up -d afterburner
docker compose up -d afterburner-reactive
docker compose up -d wrk2
docker compose up -d prometheus
