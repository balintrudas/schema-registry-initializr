# ![Schema Registry Initializr](schema-logo.png) Schema Registry Initializr

This repo provides the base java application for Schema Registry Initializr docker image. 
The images can be found on [Docker Hub](https://hub.docker.com/), and sample Docker Compose files [here](example).
The application uses the [kafka-schema-registry-maven-plugin](https://docs.confluent.io/platform/current/schema-registry/develop/maven-plugin.html) 
to read schemas from the local file system and register them on the target Schema Registry server(s). 
It can be used to initialize your schema registry's default state on start up or add more schema during runtime and reinitialize with the provided api `/schemas/init`.

## Docker Image reference

Docker images is available [here]().

## Usage

The image requires one or more configuration file (config.xml) to register schemas. 
To create a configuration file please see [kafka-schema-registry-maven-plugin register goal](https://docs.confluent.io/platform/current/schema-registry/develop/maven-plugin.html#schema-registry-register)
chapter. The required configuration file's structure and usage is completely identical to the one used in kafka-schema-registry-maven-plugin (v6.2.0).

### Config variables
With Schema Registry Initializr, all [Apache Commons Text StringSubstitutor's Interpolation](https://commons.apache.org/proper/commons-text/apidocs/org/apache/commons/text/StringSubstitutor.html) variable type declaration is supported in the config files.

```
<schemaRegistryUrls>
    <param>${env:SCHEMA_REGISTRY_URL}</param>
</schemaRegistryUrls>
```

### Examples
A sample Docker Compose file can be found [here](example/docker-compose.yml).  
The Kafka and Schema Registry compose config can be found here [here](example/docker-compose-kafka.yml).  

### Runtime registration
To register schemas during runtime just call `/schemas/init` api.  

`curl -X POST --location "http://localhost:8092/schemas/init" \
-H "Content-Type: application/json"`

## Environment variables

Variable   | Description | Default
------------- | ------------------------- | -----------
CONFIG_PATH	| Path to the configuration file or directory | config.xml
CONFIG_MERGE_STRATEGY  | If the config path is a directory, with `MERGE` strategy all config files will be merged into one config file in the files' name order. With `SEPARATE` strategy all configs will be processed separately | SEPARATE
INITIALIZE_ON_START	| Enable or disable the automatic schema registry on the startup | true
CONNECT_TIMEOUT	| The maximum time (second) to wait for schema registry url connection | 30
PORT	| Embed server port | 8092
ENABLE_API	| Enable or disable the embed api server | true

## Building the project

```
mvn clean compile
```

## Docker build

This project uses `com.google.cloud.tools:jib-maven-plugin` to build Docker images via Maven.

```
mvn jib:dockerBuild -DskipTests
```

## License
MIT: https://opensource.org/licenses/MIT
