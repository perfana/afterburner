#!/bin/sh

set -e

VERSION="${1:-1.0.0}"

docker buildx build --platform linux/amd64,linux/arm64 -t perfana/perfana-afterburner-base-image:$VERSION --push .