# Kafka connect
Kafka connect tuto, assunming your IP address is 10.11.12.64

# Requirements

### Relational Database
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
DROP TABLE IF EXISTS patient;
CREATE TABLE IF NOT EXISTS patient (
  id serial NOT NULL PRIMARY KEY,
  firstname varchar(255),
  lastname varchar(255),
  modified timestamp default CURRENT_TIMESTAMP NOT NULL
);

INSERT INTO patient (firstname, lastname) VALUES ('remy', 'TROMPIER');
```

# How to start

###### Start kafka stack
```sh
git clone kafka-connect-repo
cd kafka-connect
docker-compose up
```

###### Create a new source connector, to stream DB table into a Kafka Topic
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


###### UI
Topics : http://localhost:8000
Schema : http://localhost:8001
Connector : http://localhost:8003/





###### To delete a topic 
```
docker run \
  --net=host \
  --rm \
  confluentinc/cp-kafka:5.0.1 \
  kafka-topics --delete --topic hug-user --zookeeper localhost:2181
```

{
  "connector.class": "at.grahsl.kafka.connect.mongodb.MongoDbSinkConnector",
  "key.converter": "io.confluent.connect.avro.AvroConverter",
  "key.converter.schemas.enable": trues,
  "key.converter.schema.registry.url": "http://schema-registry:8081/",
  "mongodb.change.data.capture.handler": "at.grahsl.kafka.connect.mongodb.cdc.debezium.mongodb.MongoDbHandler",
  "mongodb.collection": "kafka",
  "mongodb.connection.uri": "mongodb://192.168.1.126:32768/test?w=1&journal=true",
  "mongodb.document.id.strategy": "at.grahsl.kafka.connect.mongodb.processor.id.strategy.BsonOidStrategy",
  "name": "output-mongo",
  "tasks.max": 1,
  "topics": "quickstart-jdbc-test",
  "value.converter": "io.confluent.connect.avro.AvroConverter",
  "value.converter.schemas.enable": true,
  "value.converter.schema.registry.url": "http://schema-registry:8081/"
}