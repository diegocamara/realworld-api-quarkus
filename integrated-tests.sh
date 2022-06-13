#!/bin/bash
java -jar target/quarkus-app/quarkus-run.jar > service.log &
SERVICE_PROCESS=$!
tail -f -n0 service.log | grep -q 'Listening on'
echo "Application started"
./collections/run-api-tests.sh
kill $SERVICE_PROCESS
rm service.log

