#!/bin/bash

POOL=employee-db-pool
echo --- hikariCP pool: $POOL
echo "aquireTotalTime: $(curl -Ss http://localhost:8080/actuator/metrics/hikaricp.connections.acquire\?tag\=pool:$POOL | jq -r ' .measurements[] | select(.statistic=="TOTAL_TIME") | .value ')"

echo "idle           : $(curl -Ss http://localhost:8080/actuator/metrics/hikaricp.connections.idle?tag=pool:$POOL | jq -r ' .measurements[] | .value ')"
echo "active         : $(curl -Ss http://localhost:8080/actuator/metrics/hikaricp.connections.active?tag=pool:$POOL | jq -r ' .measurements[] | .value ')"
