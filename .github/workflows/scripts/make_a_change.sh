#!/bin/bash

shopt -s expand_aliases

DEBUG=${RUNNER_DEBUG:-0}

debug() {
  if [ $DEBUG -ne 0 ]; then
    echo "DEBUG: ${1}";
  fi
}

issueSelected=${1}
current_run=${2}

debug "Making a change.. issueSelected: '${issueSelected}', current_run: ${current_run}"

cpu_java_file=afterburner-java/src/main/java/io/perfana/afterburner/controller/CpuBurner.java
prop_file=afterburner-java/src/main/resources/application.properties
query_file=afterburner-java/src/main/java/io/perfana/afterburner/mybatis/EmployeeMapper.java
noop_file=afterburner-java/src/main/java/io/perfana/afterburner/basket/BasketController.java

cpuIssue=0
connectionPoolIssue=0
queryIssue=0
noopIssue=0

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

function isIssuePresent() {
  debug "check if performance issue patterns are present"
  local issueFound=0
  if grep -q "some variation: is more fun" "${cpu_java_file}"; then
    debug "cpu issue found"
    cpuIssue=1
    issueFound=1
  else
    debug "cpu issue not found"
  fi

  if grep -q "afterburner.remote.call.httpclient.connections.max=2" "${prop_file}"; then
    debug "connection pool issue found"
    connectionPoolIssue=1
    issueFound=1
  else
    debug "connection pool issue not found"
  fi

  if grep -q "SELECT MAX(to_date) FROM salaries" "${query_file}"; then
    debug "query issue found"
    queryIssue=1
    issueFound=1
  else
    debug "query issue not found"
  fi

  if grep -q "description = \"Find any baskets.\"" "${noop_file}"; then
    debug "noop issue found"
    noopIssue=1
    issueFound=1
  else
    debug "noop issue not found"
  fi

  return $issueFound
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

introduce_issue() {
  if [[ $issueSelected == "cpu" ]]; then
    echo "generate cpu issue"
    debug ${cpu_java_file}
    commit_message="make matrix calculation more variable"
    edit_file "s/no variation: is no fun/some variation: is more fun/" $cpu_java_file
    edit_file 's/int funSize = matrixSize/int funSize = (int) (matrixSize \* (1.0 + (random.nextDouble() \* 3.3)))/' $cpu_java_file
  elif [[ $issueSelected == "connections" ]]; then
    echo "generate connection pool issue"
    debug ${prop_file}
    commit_message="use default httpclient connection pool size"
    edit_file "s/afterburner.remote.call.httpclient.connections.max=60/afterburner.remote.call.httpclient.connections.max=2/g" $prop_file
  elif [[ $issueSelected == "query" ]]; then
    commit_message="remove duplicates per salary"
    echo "generate query issue: ${commit_message}"
    debug ${query_file}
    edit_file "s/) limit 200;/      AND s.to_date = (SELECT MAX(to_date) FROM salaries WHERE emp_no = em.emp_no)) limit 200;/" "${query_file}"
  elif [[ $issueSelected == "baseline" ]]; then  ### toggle the change back and forth
    toggleNoopIssue
  else
    echo "This is weird... there should not be an else.."
  fi
}

if isIssuePresent -eq 0 ; then
    mod_run=0
    if [[ "$issueSelected" == "random" ]]; then
      issueTypes=("cpu" "connections" "query")
      mod_run=$(( current_run % 3 )) # run once every 4 builds
      if [[ $mod_run -ne 0 ]]; then
        issueSelected=${issueTypes[ $(( RANDOM % 2 )) ]}
      else
        issueSelected="baseline"
      fi
    fi
    ### random doesn't exist anymore: it's 'cpu' 'connections' 'query' and 'baseline'
    debug "issueSelected: ${issueSelected}, mod_run: ${mod_run}"
    introduce_issue
else
  # reset issue into a fix
  if [ $cpuIssue -eq 1 ]; then
    echo cpu issue is found, now fix it
    commit_message="make cpu more efficient "
    edit_file "s/some variation: is more fun/no variation: is no fun/g" $cpu_java_file
    edit_file "s/int funSize = (int) (matrixSize \* (1.0 + (random.nextDouble() \* 3.3)))/int funSize = matrixSize/g" $cpu_java_file
  fi

  if [ $connectionPoolIssue -eq 1 ]; then
    echo connection pool issue is found, now fix it
    commit_message="${commit_message}tune http connection pool size"
    edit_file "s/afterburner.remote.call.httpclient.connections.max=2/afterburner.remote.call.httpclient.connections.max=60/g" $prop_file
  fi

  if [ $queryIssue -eq 1 ]; then
    echo query issue is found, now fix it
    commit_message="${commit_message}fix performance issue: revert remove duplicates per salary"
    edit_file "s/      AND s.to_date = (SELECT MAX(to_date) FROM salaries WHERE emp_no = em.emp_no)) limit 200;/) limit 200;/" $query_file
  fi

  toggleNoopIssue
fi
#echo "commit_message=$commit_message" >> $GITHUB_ENV


