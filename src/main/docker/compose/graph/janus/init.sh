#!/usr/bin/env bash

while true
do
    rt=$(nc -z -w 1 cassandra 9042)
    if [ $? -eq 0 ]; then
    echo "CASSANDRA is UP" break
    fi
    echo "CASSANDRA is not yet reachable;sleep for 10s before retry" sleep 10
done
