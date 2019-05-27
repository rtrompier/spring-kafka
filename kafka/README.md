# Kafka connect
Kafka connect tuto, assunming your IP address is 10.11.12.64

# Requirements

### Relational Database
First, launch the database container
```sh
docker run -d \
  --name=quickstart-mysql \
  --net=host \
  -e MYSQL_ROOT_PASSWORD=confluent \
  -e MYSQL_USER=confluent \
  -e MYSQL_PASSWORD=confluent \
  -e MYSQL_DATABASE=connect_test \
  mysql
```

Next, Create databases and tables. You’ll need to exec into the Docker container to create the databases.

```sh
docker exec -it quickstart-mysql bash
```
On the bash prompt, create a MySQL shell

```sh
mysql -u confluent -pconfluent
```

Now, execute the following SQL statements:

```
CREATE DATABASE IF NOT EXISTS connect_test;
USE connect_test;

DROP TABLE IF EXISTS test;


CREATE TABLE IF NOT EXISTS test (
  id serial NOT NULL PRIMARY KEY,
  name varchar(100),
  email varchar(200),
  department varchar(200),
  modified timestamp default CURRENT_TIMESTAMP NOT NULL,
  INDEX `modified_index` (`modified`)
);

INSERT INTO test (name, email, department) VALUES ('alice', 'alice@abc.com', 'engineering');
INSERT INTO test (name, email, department) VALUES ('bob', 'bob@abc.com', 'sales');
INSERT INTO test (name, email, department) VALUES ('bob', 'bob@abc.com', 'sales');
INSERT INTO test (name, email, department) VALUES ('bob', 'bob@abc.com', 'sales');
INSERT INTO test (name, email, department) VALUES ('bob', 'bob@abc.com', 'sales');
INSERT INTO test (name, email, department) VALUES ('bob', 'bob@abc.com', 'sales');
INSERT INTO test (name, email, department) VALUES ('bob', 'bob@abc.com', 'sales');
INSERT INTO test (name, email, department) VALUES ('bob', 'bob@abc.com', 'sales');
INSERT INTO test (name, email, department) VALUES ('bob', 'bob@abc.com', 'sales');
INSERT INTO test (name, email, department) VALUES ('bob', 'bob@abc.com', 'sales');
exit;
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
	"name": "quickstart-jdbc-source",
	"connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector",
	"tasks.max": 1,
	"connection.url": "jdbc:mysql://YOUR IP:3306/connect_test?user=root&password=confluent",
	"mode": "incrementing",
	"incrementing.column.name": "id",
	"timestamp.column.name": "modified",
	"topic.prefix": "quickstart-jdbc-",
	"poll.interval.ms": 1000
}
```

A new topic should be automatically created named `quickstart-jdbc-test`, and all the data of the table imported into this topic.


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
  kafka-topics --delete --topic hug-user --zookeeper localhost:32181
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