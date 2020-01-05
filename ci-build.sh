#!/bin/bash
./mvnw clean package
java -jar target/realworld-api-quarkus-runner.jar > service.log &
SERVICE_PROCESS=$!
tail -f -n0 service.log | grep -q 'Listening on'
echo "Application started"
./collections/run-api-tests.sh
kill $SERVICE_PROCESS
rm service.log

