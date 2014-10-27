#!/bin/sh

export JAVA_HOME=/usr/java1.7_64

nohup ./bin/flume-ng agent -n agent -f conf/flume-conf-all-in-one.properties > spec_flume.log &

nohup ./bin/flume-ng agent -n agent -f conf/flume-conf-all-in-one_legacy-log.properties  > legacy_flume.log &