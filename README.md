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
database, connector, Kafka, and consumer one atomic operation. The consumer now identifies
a user by the source `userId` and updates the existing local projection on replay. A unique,
non-null database constraint backs that identity. Existing databases must be checked for
duplicate/missing `userId` values before applying the constraint.

Concurrent first deliveries can still race on the unique constraint and require a retry.
Events have no version/order guard, so an older event arriving later can overwrite a newer
projection. This is not general event deduplication or an exactly-once guarantee. Incrementing
JDBC polling, concurrent transaction commit order, connector recovery, and outbox retention
also need explicit design and end-to-end tests.

## Verification

With Java 11 active, run:

```bash
./gradlew check
```

JPA/H2 tests verify replayed registrations, source-user identity, updates of existing
projections, successful user/outbox commits, and rollback of a user insert when the outbox
write fails. The replay tests reproduce unique-username failures in the previous consumer.
CI runs the same checks. These tests do not start Kafka Connect, MySQL, or PostgreSQL and
do not establish end-to-end delivery or crash-recovery guarantees.

The project retains the Java 11 and Spring Boot 2.5.7 baseline listed below.

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
With Java 11 active, run the startup script from the repository root:

```bash
./start.sh
```

The script builds the services, copies connector JARs into the repository's ignored `build/`
directory, and starts the infrastructure through Docker Compose v2. It waits up to approximately
two minutes for Kafka Connect's REST API on port 8083. Tracked connector JARs are preserved.

Compose starts infrastructure only. In separate terminals, start both applications:

```bash
./gradlew :ms-user:bootRun
./gradlew :ms-product:bootRun
```

The user service uses port 8085; the product service uses port 8086. Wait for their startup
and Hibernate table initialization, then register the connector:

```bash
sh ./database-source-connector.sh
```

Registration fails on HTTP errors. The request creates `jdbc-source-connector`; if it already
exists, inspect or remove that existing connector before registering it again. A successful
registration request is not proof that the connector task is running; inspect its status at
`http://localhost:8083/connectors/jdbc-source-connector/status`.
