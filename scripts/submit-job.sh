#!/bin/sh
# $1 = path to jar
flink run -m flink-jobmanager:8081 "$1"