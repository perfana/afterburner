#!/bin/sh

# default SpringBoot: localhost:8080
HOST=${1:-afterburner}
# default SpringBoot: threadPoolTaskScheduler
EXECUTOR=${2:-afterburner-executor}

echo "host: $HOST executor: $EXECUTOR"
echo "queued:          $(curl -s $HOST/actuator/metrics/executor.queued?tag=name:$EXECUTOR | jq -C '{ queued: .measurements[0] | .value }[]')"
echo "queue.remaining: $(curl -s $HOST/actuator/metrics/executor.queue.remaining?tag=name:$EXECUTOR | jq -C '{ remaining: .measurements[0] | .value }[]')"
echo "active:          $(curl -s $HOST/actuator/metrics/executor.active?tag=name:$EXECUTOR | jq -C '{ active: .measurements[0] | .value }[]')"
echo "pool.size:       $(curl -s $HOST/actuator/metrics/executor.pool.size?tag=name:$EXECUTOR | jq -C '{ poolsize: .measurements[0] | .value }[]')"
echo "pool.core:       $(curl -s $HOST/actuator/metrics/executor.pool.core?tag=name:$EXECUTOR | jq -C '{ poolcore: .measurements[0] | .value }[]')"
echo "pool.max:        $(curl -s $HOST/actuator/metrics/executor.pool.max?tag=name:$EXECUTOR | jq -C '{ poolmax: .measurements[0] | .value }[]')" 
echo "completed:       $(curl -s $HOST/actuator/metrics/executor.completed?tag=name:$EXECUTOR | jq -C '{ completed: .measurements[0] | .value }[]')" 

