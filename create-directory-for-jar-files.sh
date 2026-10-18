#!/bin/sh
set -eu

cd "$(dirname "$0")"
mkdir -p build/kafka-connect-plugins/jdbc
cp connector-jar/kafka-connect-jdbc-10.0.0.jar build/kafka-connect-plugins/jdbc/
cp connector-jar/mysql-connector-j-8.0.33.jar build/kafka-connect-plugins/jdbc/
