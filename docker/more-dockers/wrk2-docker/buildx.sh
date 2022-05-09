#!/bin/bash

docker buildx build --platform linux/amd64,linux/arm64 -t stokpop/wrk2:0.0.3 --push .
