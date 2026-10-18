![outbox_pattern drawio](https://github.com/user-attachments/assets/d5522ee6-cd33-4c21-9645-972d1ef3a50f)

# Spring Boot Transactional Outbox Example

A learning example that writes a user and an outbox row in one database transaction,
then uses Kafka Connect to publish outbox rows for a second service to consume.

## Message flow

1. `ms-user` saves the user and outbox row in the same Spring `@Transactional` method.
2. A Kafka Connect JDBC source polls the MySQL `users_outbox` table every five seconds,
   tracking the incrementing `id` column.
3. `ms-product` consumes the `db-users_outbox` topic and saves a local user record.

The connector uses JDBC polling, not transaction-log change data capture.

## Guarantees and limitations

The local transaction couples the user write to the outbox insert. It does not make the
database, connector, Kafka, and consumer one atomic operation. The consumer save path has
no explicit event deduplication step, so duplicate delivery needs to be handled before this
is used beyond an example. Concurrent transaction commit order, connector recovery, and
outbox retention also need explicit design and tests.

No automated test sources or CI workflow are currently committed. The project uses the
Java 11 and Spring Boot 2.5.7 baseline listed below.

## Tech Stack

* Java 11
* Spring Boot 2.5.7
* Spring Data JPA
* Kafka
* Kafka Connect
* Mysql
* PostgreSQL
* Docker
* Lombok

## Build & Run
From the repository root, make the Gradle wrapper executable and run the startup script:

```bash
chmod +x gradlew
sh ./start.sh
```

The script builds the services, prepares connector JARs, starts Docker Compose, and submits
the JDBC connector configuration. It expects the legacy `docker-compose` executable.
Connector registration runs immediately after Compose starts; if Kafka Connect is not ready,
wait for its REST API on port 8083 and retry `sh ./database-source-connector.sh`.
