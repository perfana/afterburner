#!/bin/sh

POOL=employee-db-pool
HOST=${1:-localhost}

echo --- hikariCP pool: $POOL
echo "acquireTotalTime: $(curl -Ss $HOST/actuator/metrics/hikaricp.connections.acquire\?tag\=pool:$POOL | jq -r ' .measurements[] | select(.statistic=="TOTAL_TIME") | .value ')"

echo "idle            : $(curl -Ss $HOST/actuator/metrics/hikaricp.connections.idle?tag=pool:$POOL | jq -r ' .measurements[] | .value ')"
echo "active          : $(curl -Ss $HOST/actuator/metrics/hikaricp.connections.active?tag=pool:$POOL | jq -r ' .measurements[] | .value ')"
echo "timeouts        : $(curl -Ss $HOST/actuator/metrics/hikaricp.connections.timeout?tag=pool:$POOL | jq -r ' .measurements[] | .value ')"