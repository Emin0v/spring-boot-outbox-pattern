curl -X POST -H "Content-Type: application/json" --data '{
  "name": "jdbc-source-connector",
  "config": {
    "connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector",
    "tasks.max": "1",
    "connection.url": "jdbc:mysql://mysql:3306/userMsDb",
    "connection.user": "root",
    "connection.password": "password",
    "table.whitelist": "users_outbox",
    "mode": "incrementing",
    "incrementing.column.name": "id",
    "topic.prefix": "db-",
    "poll.interval.ms": "5000",

    "key.converter": "org.apache.kafka.connect.storage.StringConverter",
    "value.converter": "org.apache.kafka.connect.json.JsonConverter",
    "value.converter.schemas.enable": "false",

    "transforms": "ExtractField",
    "transforms.ExtractField.type": "org.apache.kafka.connect.transforms.ExtractField$Key",
    "transforms.ExtractField.field": "id"
  }
}' http://localhost:8083/connectors
