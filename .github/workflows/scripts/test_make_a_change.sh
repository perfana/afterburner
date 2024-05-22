#!/usr/bin/env bash

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

oneTimeTearDown() {
  # No matter what the end-state, revert changed files
  git checkout "${cpu_java_file} ${prop_file} ${query_file} ${noop_file}" &> /dev/null
}

testBaselineWithoutPreexistingIssue(){
  for i in {0..3}; do
#    echo "Test build $i"
    result=$($SCRIPT baseline $i)
    assertContains "Change forward" "$result" "generate noop issue: change the description"
    result=$($SCRIPT baseline $((i+1)))
    assertContains "Change revert" "$result" "generate noop issue: revert the description"
  done
}

testBaselineWithExistingCpuIssue(){
  for i in {0..3}; do
    introduce_cpu_issue
    result=$($SCRIPT baseline 1)
    assertContains "Change forward" "$result" "generate noop issue: change the description"
  done
}

#testBaselineWithExistingConnectionsIssue(){
#  result=$($SCRIPT baseline 1)
#  assertContains "Change forward" "$result" "generate noop issue: change the description"
#  result=$($SCRIPT baseline 1)
#  assertContains "Change revert" "$result" "generate noop issue: revert the description"
#}
#testBaselineWithExistingQueryIssue(){
#  result=$($SCRIPT baseline 1)
#  assertContains "Change forward" "$result" "generate noop issue: change the description"
#  result=$($SCRIPT baseline 1)
#  assertContains "Change revert" "$result" "generate noop issue: revert the description"
#}
#


# a no-op issue IS present
# a cpu issue IS present
# a connections issue IS present
# a query issue IS present

# Make sure 'shunit2' is on your PATH
# See: https://github.com/kward/shunit2
. shunit2

