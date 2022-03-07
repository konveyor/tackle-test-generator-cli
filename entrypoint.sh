#!/usr/bin/env bash
set -e

usage() {
    echo
    echo "Usage: tkltest-unit|tkltest-ui [options]"
    echo
    echo "Commands:"
    echo "    tkltest-unit      Run tkltest-unit CLI with specified options"
    echo "    tkltest-ui        Run tkltest-ui CLI with specified options"
    echo "    --help, -h        Show this help message and exit"
    echo
}

if [[ $# -eq 0 ]]; then
    usage
    exit
fi

case $1 in
    tkltest-unit|tkltest-ui)
        exec "$@"
    ;;
    -h|--help)
        usage
        exit
    ;;
    *)
        printf 'ERROR: Unknown command: %s\n' "$1"
        usage
        exit 1
    ;;
esac
