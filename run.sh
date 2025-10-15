#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"

export JAVA_HOME="/home/jvullo/.sdkman/candidates/java/25.0.3-tem"
export PATH="$JAVA_HOME/bin:$PATH"

mvn -q package -DskipTests
java --enable-preview -jar target/switchyard-service-0.1.0.jar
