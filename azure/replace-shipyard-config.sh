#!/bin/bash
cd config
perl -pe 's/\[([_A-Z]+)\]/$ENV{$1}/g' <config.template.yaml >config.yaml
perl -pe 's/\[([_A-Z]+)\]/$ENV{$1}/g' <credentials.template.yaml >credentials.yaml
perl -pe 's/\[([_A-Z]+)\]/$ENV{$1}/g' <pool.template.yaml >pool.yaml
perl -pe 's/\[([_A-Z]+)\]/$ENV{$1}/g' <jobs.template.yaml >jobs.yaml
cd - &>/dev/null
