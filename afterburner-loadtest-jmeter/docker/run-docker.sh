#!/usr/bin/env bash
export timestamp=$(date +%Y%m%d_%H%M%S) && \
export volume_path=${JMETER_FILES_PATH} && \
export jmeter_path=/mnt/jmeter && \
export jmeter_script_name=${JMETER_SCRIPT_NAME} && \
export jmeter_jmx_domain=${JMETER_JMX_DOMAIN}

docker run \
  --volume "${volume_path}":"${jmeter_path}" \
  jmeter \
  -n \
  -Jjmx.domain=${jmeter_jmx_domain} \
  -t ${jmeter_path}/${jmeter_script_name} \
  -l ${jmeter_path}/tmp/result_${timestamp}.jtl \
  -j ${jmeter_path}/tmp/jmeter_${timestamp}.log
