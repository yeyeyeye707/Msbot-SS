#!/usr/bin/env sh

mvn -f msbot-java clean package &&
docker-compose -f docker-compose-msbot.yml build --no-cache
