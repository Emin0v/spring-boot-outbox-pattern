#!/bin/sh
set -eu

cd "$(dirname "$0")"
./gradlew clean build
sh ./create-directory-for-jar-files.sh
docker compose up -d

attempt=0
until curl -fsS --max-time 2 http://localhost:8083/connectors >/dev/null; do
    attempt=$((attempt + 1))
    if [ "$attempt" -ge 30 ]; then
        echo "Kafka Connect did not become ready on port 8083" >&2
        exit 1
    fi
    sleep 2
done

echo "Infrastructure ready. Start ms-user and ms-product with Gradle bootRun."
echo "After their tables are initialized, run: sh ./database-source-connector.sh"
