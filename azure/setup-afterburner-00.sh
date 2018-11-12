#!/usr/bin/env bash

is_command_available () {
    command -v "$1" &>/dev/null
}

if ! is_command_available "java"; then
    echo "Java (java) command must be available"
    exit 1
fi

if ! is_command_available "docker"; then
    echo "Docker (docker) command must be available"
    exit 1
fi

if ! is_command_available "az"; then
    echo "Azure-CLI (az) command must be available"
    exit 1
fi

source setup-afterburner-01-exports.sh
source setup-afterburner-02-creates.sh
source setup-afterburner-03-exports.sh
source setup-afterburner-04-build-and-push-image.sh
source setup-afterburner-05-create-webapp.sh
