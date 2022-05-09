#!/bin/bash

export COMPOSE_PROJECT_NAME=workshop

ACTIVATE_EMPLOYEE_DB=false

# update wrk2 image for apple silicon
if [[ $(uname -m) == 'arm64' ]]; then
  echo "Detected Apple Silicon M1, update wrk2 to M1 compatible container"
  sed -i '' 's/wrk2:0.0.3/wrk2-aarch:0.0.2/g' docker-compose.yml
fi
 
mkdir -p -v dumps
mkdir -p -v byteman
if [ "$ACTIVATE_EMPLOYEE_DB" = true ]; then
    mkdir -p -v mariadb
fi

if [[ ! -f employee-db/test_db/employees.sql && "$ACTIVATE_EMPLOYEE_DB" = true ]]; then
    echo "First download and unpack employees db"
    ./download-emp-db.sh
fi

if [ "$ACTIVATE_EMPLOYEE_DB" = true ]; then
    docker compose up -d mariadb
fi
docker compose up -d jaeger
docker compose up -d afterburner
docker compose up -d afterburner-reactive
docker compose up -d wrk2
docker compose up -d prometheus

