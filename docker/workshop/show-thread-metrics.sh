#!/bin/sh

# default SpringBoot: localhost:8080
HOST=${1:-afterburner}

echo "host: $HOST"
echo "waiting:       $(curl -s $HOST/actuator/metrics/jvm.threads.states\?tag\=state:waiting | jq -C '{ waiting: .measurements[0] | .value }[]')"
echo "runnable:      $(curl -s $HOST/actuator/metrics/jvm.threads.states\?tag\=state:runnable | jq -C '{ runnable: .measurements[0] | .value }[]')"
echo "timed-waiting: $(curl -s $HOST/actuator/metrics/jvm.threads.states\?tag\=state:timed-waiting | jq -C '{ timedwaiting: .measurements[0] | .value }[]')"
echo "new:           $(curl -s $HOST/actuator/metrics/jvm.threads.states\?tag\=state:new | jq -C '{ new: .measurements[0] | .value }[]')"
echo "blocked:       $(curl -s $HOST/actuator/metrics/jvm.threads.states\?tag\=state:blocked | jq -C '{ blocked: .measurements[0] | .value }[]')"
