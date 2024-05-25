#!/usr/bin/env bash

# Make sure 'shunit2' is on your PATH
# See: https://github.com/kward/shunit2

# Interesting to check out:
# ACT: https://nektosact.com - run GH Actions locally

export RUNNER_DEBUG=1

# load common functions
source .github/workflows/scripts/common.sh
SCRIPT='./.github/workflows/scripts/make_a_change.sh'

# a baseline run should introduce a no-op change to force a build
#test baseline 0 "BASELINE"
#test baseline 1 "Make a no-op change." "banaan"
#test baseline 2 "Make a no-op change." "banaan"
#test baseline 3 "Make a no-op change." "banaan"

#test cpu 1 "DEBUG: cpu issue not found" "DEBUG: connection pool issue not found" "DEBUG: query issue not found" "DEBUG: noop issue not found"
#test connections 1 "DEBUG: cpu issue not found" "DEBUG: connection pool issue not found" "DEBUG: query issue not found" "DEBUG: noop issue not found"
#test query 1 "DEBUG: cpu issue not found" "DEBUG: connection pool issue not found" "DEBUG: query issue not found" "DEBUG: noop issue not found"
#test noop 1 "DEBUG: cpu issue not found" "DEBUG: connection pool issue not found" "DEBUG: query issue not found" "DEBUG: noop issue not found"

setUp() {
  echo "Setup: revert previous changes"
  # No matter what the end-state, revert changed files
  files=("${cpu_java_file}" "${prop_file}" "${query_file}" "${noop_file}")
  for i in "${files[@]}"; do
#    debug "Reverting $i"
    git checkout $i &> /dev/null
  done
}

testIntroduceCpuIssue() {
  introduce_cpu_issue
  assertContains "$(cat ${cpu_java_file})" "some variation: is more fun"
}

_testIntroduceConnectionsIssue() {
  introduce_connections_issue
  assertContains "$(cat ${prop_file})" "afterburner.remote.call.httpclient.connections.max=2"
}

_testIntroduceQueryIssue() {
  introduce_query_issue
  assertContains "$(cat ${query_file})" "AND s.to_date = (SELECT MAX(to_date) FROM salaries WHERE emp_no = em.emp_no)) limit 200"
}

_testBaselineWithoutPreexistingIssue(){
  for i in {0..3}; do
#    echo "Test build $i"
    result=$($SCRIPT baseline $i)
    assertContains "Change forward" "$result" "generate noop issue: change the description"
    result=$($SCRIPT baseline $((i+1)))
    assertContains "Change revert" "$result" "generate noop issue: revert the description"
  done
}

### when an issue is present, baseline should remove the issue
testBaselineWithExistingCpuIssue(){
  introduce_cpu_issue
  result=$($SCRIPT baseline 1 | tee /dev/fd/2)
  assertContains "$result" "cpu issue found"
  assertContains "$result" "connection pool issue not found"
  assertContains "$result" "query issue not found"
  assertContains "$result" "cpu issue is found, now fix it"
}

### when an issue is present, baseline should remove the issue
testBaselineWithExistingConnectionsIssue(){
  introduce_connections_issue
  result=$($SCRIPT baseline 1 | tee /dev/fd/2)
  assertContains "$result" "cpu issue not found"
  assertContains "$result" "connection pool issue found"
  assertContains "$result" "query issue not found"
  assertContains "$result" "connection pool issue is found, now fix it"
}

### when an issue is present, baseline should remove the issue
testBaselineWithExistingQueryIssue(){
  introduce_query_issue
  result=$($SCRIPT baseline 1 | tee /dev/fd/2)
  assertContains "$result" "cpu issue not found"
  assertContains "$result" "connection pool issue not found"
  assertContains "$result" "query issue found"
  assertContains "$result" "query issue is found, now fix it"
}

### when an issue is not present, baseline should introduce a noop change



# a no-op issue IS present
# a cpu issue IS present
# a connections issue IS present
# a query issue IS present

# Make sure 'shunit2' is on your PATH
# See: https://github.com/kward/shunit2

. shunit2