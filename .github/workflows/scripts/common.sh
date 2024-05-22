#!/bin/bash

shopt -s expand_aliases

DEBUG=${RUNNER_DEBUG:-0}

cpu_java_file=afterburner-java/src/main/java/io/perfana/afterburner/controller/CpuBurner.java
prop_file=afterburner-java/src/main/resources/application.properties
query_file=afterburner-java/src/main/java/io/perfana/afterburner/mybatis/EmployeeMapper.java
noop_file=afterburner-java/src/main/java/io/perfana/afterburner/basket/BasketController.java

debug() {
  if [ $DEBUG -ne 0 ]; then
    echo "DEBUG: ${1}";
  fi
}

# Function to detect the operating system and apply the appropriate sed syntax
edit_file() {
  local file=$2
  local sed_command=$1

  if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    sed -i '' "${sed_command}" "$file"
  else
    # Linux and other Unix-like systems
    sed -i "${sed_command}" "$file"
  fi
}

introduce_cpu_issue() {
  echo "generate cpu issue"
  debug ${cpu_java_file}
  commit_message="make matrix calculation more variable"
  edit_file "s/no variation: is no fun/some variation: is more fun/" $cpu_java_file
  edit_file 's/int funSize = matrixSize/int funSize = (int) (matrixSize \* (1.0 + (random.nextDouble() \* 3.3)))/' $cpu_java_file
}

introduce_connections_issue() {
  echo "generate connection pool issue"
  debug ${prop_file}
  commit_message="use default httpclient connection pool size"
  edit_file "s/afterburner.remote.call.httpclient.connections.max=60/afterburner.remote.call.httpclient.connections.max=2/g" $prop_file
}

introduce_query_issue() {
  commit_message="remove duplicates per salary"
  echo "generate query issue: ${commit_message}"
  debug ${query_file}
  edit_file "s/) limit 200;/      AND s.to_date = (SELECT MAX(to_date) FROM salaries WHERE emp_no = em.emp_no)) limit 200;/" "${query_file}"
}

toggleNoopIssue() {
  debug ${noop_file}
  if [[ $noopIssue -eq 0 ]]; then
    commit_message="change the description"
    echo "generate noop issue: ${commit_message}"
    edit_file "s/description = \"Find all baskets.\"/description = \"Find any baskets.\"/" "${noop_file}"
  else
    commit_message="revert the description"
    echo "generate noop issue: ${commit_message}"
    edit_file "s/description = \"Find any baskets.\"/description = \"Find all baskets.\"/" "${noop_file}"
  fi
}
