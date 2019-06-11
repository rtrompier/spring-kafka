# Kafka connect
Kafka connect tuto, assunming your IP address is 192.168.1.6


## Stream a Relational Database with JDBC Connector
First, launch the database container
```sh
docker run -d \
  --name postgres \
  -p 5432:5432 \
  -e POSTGRES_USER=root \
  -e POSTGRES_PASSWORD=confluent \
  -e POSTGRES_DB=patient \
  postgres
```

Next, Create databases and tables. You’ll need to execute the following SQL statements

```
ALTER SCHEMA public RENAME TO hcuge;

DROP TABLE IF EXISTS patient;
CREATE TABLE IF NOT EXISTS patient (
  id serial NOT NULL PRIMARY KEY,
  firstname varchar(255),
  lastname varchar(255),
  modified timestamp default CURRENT_TIMESTAMP NOT NULL
);

INSERT INTO hcuge.patient (firstname, lastname) VALUES ('remy', 'TROMPIER');
```

### Start kafka stack
```sh
cd kafka
docker-compose up
```

UI Available : 
 - Topics : http://localhost:8000
 - Schema : http://localhost:8001
 - Connector : http://localhost:8003/


### Create a new source connector, to stream DB table into a Kafka Topic
Go to Kafka Connect UI, and create a new JDBC Connector, by past the folowing configuration.

```json
{
  "name": "patient-jdbc-source",
  "connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector",
  "tasks.max": 1,
  "connection.url": "jdbc:postgresql://192.168.1.6:5432/patient?user=root&password=confluent",
  "mode": "incrementing",
  "incrementing.column.name": "id",
  "timestamp.column.name": "modified",
  "topic.prefix": "jdbc-",
  "poll.interval.ms": 1000,
  "transforms":"createKey,extractInt,addNamespace",
  "transforms.createKey.type":"org.apache.kafka.connect.transforms.ValueToKey",
  "transforms.createKey.fields":"id",
  "transforms.extractInt.type":"org.apache.kafka.connect.transforms.ExtractField$Key",
  "transforms.extractInt.field":"id",
  "transforms.addNamespace.type":"org.apache.kafka.connect.transforms.SetSchemaMetadata$Value",
  "transforms.addNamespace.schema.name": "ch.hcuge.kafka.Patient"
}
```

A new topic should be automatically created named `jdbc-patient`, and all the data of the table imported into this topic.


## Stream DB with Debezium connector
Debezium is a distributed platform that turns your existing databases into event streams, so applications can see and respond immediately to each row-level change in the databases.

First, launch the custom Postgres database container.
See here why we use a custom DB : https://hub.docker.com/r/debezium/postgres

```sh
docker stop postgres && docker rm postgres
docker run -d \
  --name postgres \
  -p 5432:5432 \
  -e POSTGRES_USER=root \
  -e POSTGRES_PASSWORD=confluent \
  -e POSTGRES_DB=patient \
  debezium/postgres
```

Next, Create databases and tables. You’ll need to execute the following SQL statements
Public schema is renamed to avoid conflict with a 'public' package in java.
```
ALTER SCHEMA public RENAME TO hcuge;

DROP TABLE IF EXISTS patient;
CREATE TABLE IF NOT EXISTS patient (
  id serial NOT NULL PRIMARY KEY,
  firstname varchar(255),
  lastname varchar(255),
  modified timestamp default CURRENT_TIMESTAMP NOT NULL
);

INSERT INTO hcuge.patient (firstname, lastname) VALUES ('remy', 'TROMPIER');
```

Next, add the connector to Kafka connect : 

```sh
curl -i -X POST \
  -H "Accept:application/json" \
  -H "Content-Type:application/json" \
  localhost:8083/connectors/ \
  -d '{ "name": "patient-connector", "config": { "connector.class": "io.debezium.connector.postgresql.PostgresConnector", "database.hostname": "192.168.1.6", "database.port": "5432", "database.user": "root", "database.password": "confluent", "database.dbname" : "patient", "database.server.name": "ch.hcuge.kafka", "table.whitelist": "hcuge.patient", "database.history.kafka.bootstrap.servers": "kafka:9092", "database.history.kafka.topic": "dbhistory.patient" } }'
```

Next, start a java program to be notified when a row is added, updated, or deleted into the database.
Udapte `schema-registry.url` properties inside the pom.xml, and `application.yml` file if necessary and execute :

```sh
mvn clean schema-registry:download generate-sources package -DskipTests
java -jar target/kafka-0.0.1-SNAPSHOT.jar
```

For information, `schema-registry:download generate-sources` will automatically download the Avro schema from the schema-registry, and generated the corresponding Java class.

## To delete a topic 
```
docker run \
  --net=host \
  --rm \
  confluentinc/cp-kafka:5.0.1 \
  kafka-topics --delete --topic jdbc-patient --zookeeper localhost:2181
```
