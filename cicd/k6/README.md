# afterburner k6 test running in container

Example of how to run a k6 script in a container, using a kubernetes job, orchestrated by the events-scheduler plugin combine with the CommandRunner plugin 
# Build script container

```docker build -t perfana/k6-afterburner:1.0.0 .```

# Kubernetes job manifest

```apiVersion: batch/v1
kind: Job
metadata:
  name: k6
spec:
  template:
    spec:
      restartPolicy: Never
      containers:
        - name: k6
          image: "perfana/k6-afterburner:1.0.0"
          imagePullPolicy: Always
          env:
            - name: K6_INFLUXDB_URL
              valueFrom:
                secretKeyRef:
                  name: k6-args
                  key: K6_INFLUXDB_URL
            - name: K6_INFLUXDB_USERNAME
              valueFrom:
                secretKeyRef:
                  name: k6-args
                  key: K6_INFLUXDB_USERNAME
            - name: K6_INFLUXDB_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: k6-args
                  key: K6_INFLUXDB_PASSWORD
            - name: CONSTANT_LOAD_TIME
              valueFrom:
                secretKeyRef:
                  name: k6-args
                  key: CONSTANT_LOAD_TIME
            - name: RAMPUP_TIME
              valueFrom:
                secretKeyRef:
                  name: k6-args
                  key: RAMPUP_TIME
            - name: START_RATE
              valueFrom:
                secretKeyRef:
                  name: k6-args
                  key: START_RATE
            - name: TARGET_RATE
              valueFrom:
                secretKeyRef:
                  name: k6-args
                  key: TARGET_RATE
            - name: TARGET_BASE_URL
              valueFrom:
                secretKeyRef:
                  name: k6-args
                  key: TARGET_BASE_URL
            - name: SYSTEM_UNDER_TEST
              valueFrom:
                secretKeyRef:
                  name: k6-args
                  key: SYSTEM_UNDER_TEST
            - name: TEST_ENVIRONMENT
              valueFrom:
                secretKeyRef:
                  name: k6-args
                  key: TEST_ENVIRONMENT
            - name: TEST_RUN_ID
              valueFrom:
                secretKeyRef:
                  name: k6-args
                  key: TEST_RUN_ID

          args:
            - "run"
            - "--out"
            - "influxdb=$(K6_INFLUXDB_URL)"
            - "-e"
            - "K6_INFLUXDB_USERNAME=$(K6_INFLUXDB_USERNAME)"
            - "-e"
            - "K6_INFLUXDB_PASSWORD=$(K6_INFLUXDB_PASSWORD)"
            - "-e"
            - "CONSTANT_LOAD_TIME=$(CONSTANT_LOAD_TIME)"
            - "-e"
            - "RAMPUP_TIME=$(RAMPUP_TIME)"
            - "-e"
            - "START_RATE=$(START_RATE)"
            - "-e"
            - "TARGET_RATE=$(TARGET_RATE)"
            - "-e"
            - "TARGET_BASE_URL=$(TARGET_BASE_URL)"
            - "-e"
            - "SYSTEM_UNDER_TEST=$(SYSTEM_UNDER_TEST)"
            - "-e"
            - "TEST_ENVIRONMENT=$(TEST_ENVIRONMENT)"
            - "-e"
            - "TEST_RUN_ID=$(TEST_RUN_ID)"
            - "/home/k6/script.js"
```

# CommandRunner plugin configuration

* command: create secret to pass arguments to kubernetes job and create the kubernetes job based on the manifest
* pollingCommand: command to check if job is still active, exitcode = 0 when active
* abortCommand: delete job in case of abort event

``` <eventConfig implementation="io.perfana.events.commandrunner.CommandRunnerEventConfig">
        <name>Run k6 job</name>
        <continueOnKeepAliveParticipant>true</continueOnKeepAliveParticipant>
        <command>sh -c "kubectl delete secret k6-args --ignore-not-found; \
            kubectl create secret generic k6-args -n acme \
            --from-literal=K6_INFLUXDB_URL=${influxUrl} \
            --from-literal=K6_INFLUXDB_PASSWORD=${influxPassword} \
            --from-literal=K6_INFLUXDB_USERNAME=${influxUser} \
            --from-literal=CONSTANT_LOAD_TIME=${duration}s \
            --from-literal=RAMPUP_TIME=${rampUpTime}s \
            --from-literal=START_RATE=${startRate} \
            --from-literal=TARGET_RATE=${targetRate} \
            --from-literal=TARGET_BASE_URL=${targetBaseUrl} \
            --from-literal=SYSTEM_UNDER_TEST=${systemUnderTest} \
            --from-literal=TEST_ENVIRONMENT=${testEnvironment} \
            --from-literal=TEST_RUN_ID=${testRunId}; \
            kubectl delete jobs k6 -n acme --ignore-not-found; \
            kubectl create -f k6-job.yml -n acme"
        </command>
        <pollingCommand>sh -c "kubectl get jobs k6 -o jsonpath={$.status.active} | grep -q 1"</pollingCommand>
        <abortCommand>sh -c "kubectl delete jobs k6 -n acme --ignore-not-found"</abortCommand>
    </eventConfig>```