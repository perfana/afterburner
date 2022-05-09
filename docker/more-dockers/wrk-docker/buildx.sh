#!/bin/bash

docker buildx build --platform linux/amd64,linux/arm64 -t stokpop/wrk:0.0.1 --push .
