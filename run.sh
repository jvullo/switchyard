#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"

mvn -q package -DskipTests
java --enable-preview -jar target/switchyard-service-0.1.0.jar
