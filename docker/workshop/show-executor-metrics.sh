#!/bin/sh

HOST=${1:-localhost:8080}

echo "active:    $(curl -s $HOST/actuator/metrics/executor.active | jq -C '{ active: .measurements[] | .value }[]')"
echo "queued:    $(curl -s $HOST/actuator/metrics/executor.queued | jq -C '{ queued: .measurements[] | .value }[]')"
echo "idle:      $(curl -s $HOST/actuator/metrics/executor.idle | jq -C '{ idle: .measurements[0] | .value }[]')" 
echo "pool.core: $(curl -s $HOST/actuator/metrics/executor.pool.core | jq -C '{ poolcore: .measurements[] | .value }[]')"
echo "pool.max:  $(curl -s $HOST/actuator/metrics/executor.pool.max | jq -C '{ poolmax: .measurements[] | .value }[]')" 
echo "pool.size: $(curl -s $HOST/actuator/metrics/executor.pool.size | jq -C '{ poolsize: .measurements[] | .value }[]')"

