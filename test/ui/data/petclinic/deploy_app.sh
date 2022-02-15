#!/usr/bin/env bash

DOCKER_IMAGE_NAME_REST="springcommunity/spring-petclinic-rest"
DOCKER_IMAGE_NAME_ANGULAR="spring-petclinic-angular:latest"
DOCKER_CONTAINER_NAME_REST="petclinic-rest"
DOCKER_CONTAINER_NAME_ANGULAR="petclinic-angular"

usage() {
    echo
    echo "Usage: $0 [command]"
    echo "  start [-b/--build]    start app container, optionally building the image"
    echo "  stop                  stop app container"
    echo
}

stop() {
    echo "=> Stopping and removing existing $DOCKER_CONTAINER_NAME_ANGULAR container"
    docker rm -f $DOCKER_CONTAINER_NAME_ANGULAR

    echo "=> Stopping and removing existing $DOCKER_CONTAINER_NAME_REST container"
    docker rm -f $DOCKER_CONTAINER_NAME_REST
    sleep 1
}

start() {
    stop
    echo "=> Running $DOCKER_CONTAINER_NAME_REST container"
    docker run -d -p 9966:9966 --name $DOCKER_CONTAINER_NAME_REST $DOCKER_IMAGE_NAME_REST
    sleep 3

    if [[ $# -eq 1 ]]; then
        echo "=> Building $DOCKER_IMAGE_NAME_ANGULAR"
        cd ../webapps/spring-petclinic-angular
        docker build -t spring-petclinic-angular:latest .
    fi
    echo "=> Running $DOCKER_CONTAINER_NAME_ANGULAR container"
    docker run -d -p 8080:8080 --name $DOCKER_CONTAINER_NAME_ANGULAR $DOCKER_IMAGE_NAME_ANGULAR
    sleep 1
}

if [[ $# -eq 0 || $# > 2 ]]; then
    usage
    exit 1
fi

if [[ $1 == "stop" ]]; then
    stop
elif [[ $1 == "start" ]]; then
    if [[ $# -eq 2 ]]; then
        if [[ $2 != "-b" && $2 != "--build" ]]; then
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
