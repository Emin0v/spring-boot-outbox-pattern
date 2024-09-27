./gradlew clean build \
&& /bin/sh create-directory-for-jar-files.sh \
&& docker-compose up -d \
&& /bin/sh database-source-connector.sh