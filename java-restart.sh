#!/usr/bin/env sh

./java-stop.sh &&
 docker-compose -f docker-compose-msbot.yml up -d