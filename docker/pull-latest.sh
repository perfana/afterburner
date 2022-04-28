#!/bin/bash
bash stop.sh
git stash && git pull && git stash apply
