#!/bin/bash

mkdir -p -v dumps
mkdir -p -v byteman
mkdir -p -v mariadb

if [ ! -f employee-db/test_db/employees.sql ]; then
    echo "First download and unpack employees db"
    ./download-emp-db.sh
fi

export COMPOSE_PROJECT_NAME=workshop

docker compose up -d mariadb
docker compose up -d jaeger
docker compose up -d afterburner
docker compose up -d afterburner-reactive
docker compose up -d wrk2
docker compose up -d prometheus

