#!/usr/bin/env bash

DOCKER_IMAGE_NAME="dockercontainervm/addressbook"
DOCKER_IMAGE_TAG='8.0.0.0'
DOCKER_CONTAINER_NAME="addressbook"

usage() {
    echo
    echo "Usage: $0 [command]"
    echo "  start [-p/--pull]     start app container, optionally pulling a fresh image"
    echo "  stop                  stop app container"
    echo
}

stop() {
    echo "=> Stopping and removing existing $DOCKER_CONTAINER_NAME container"
    docker rm -f $DOCKER_CONTAINER_NAME
    sleep 1
}

start() {
    stop
    if [[ $# -eq 1 ]]; then
        docker rmi --force $DOCKER_IMAGE_NAME:$DOCKER_IMAGE_TAG
    fi
    echo "=> Running $DOCKER_CONTAINER_NAME container"
    cd ../webapps/addressbook
    ./run-docker.sh -p yes -n $DOCKER_CONTAINER_NAME
    sleep 3
}

if [[ $# -eq 0 || $# > 2 ]]; then
    usage
    exit 1
fi

if [[ $1 == "stop" ]]; then
    stop
elif [[ $1 == "start" ]]; then
    if [[ $# -eq 2 ]]; then
        if [[ $2 != "-p" && $2 != "--pull" ]]; then
            echo "Unknown start option: $2"
            usage
            exit 1
        fi
        start $2
    else
        start
    fi
else
    echo "Unknown command: $1"
    usage
fi
