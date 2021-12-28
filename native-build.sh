#!/bin/bash
set -x
./mvnw package -Dmaven.test.skip=true -Dquarkus.package.type=native-sources 
cp native-image.args target/native-sources/
cd target/native-sources
time docker run -it --rm -v $(pwd):/work -w /work --entrypoint /bin/sh ghcr.io/graalvm/native-image -c "native-image $(cat native-image.args) -J-Xmx5g"
mv target/native-sources/realworld-api-quarkus-runner target/
