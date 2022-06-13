# ![RealWorld Example App](quarkus-logo.png)

> ### Quarkus Framework codebase containing real world examples (CRUD, auth, advanced patterns, etc) that adheres to the [RealWorld](https://github.com/gothinkster/realworld) spec and API.

This codebase was created to demonstrate a fully fledged fullstack application built with [Quarkus](https://quarkus.io/)
including CRUD operations, authentication, routing, pagination, and more.

We've gone to great lengths to adhere to the Quarkus community styleguides & best practices.

For more information on how to this works with other frontends/backends, head over to
the [RealWorld](https://github.com/gothinkster/realworld) repo.

[![Java CI with Maven](https://github.com/diegocamara/realworld-api-quarkus/actions/workflows/maven.yml/badge.svg)](https://github.com/diegocamara/realworld-api-quarkus/actions/workflows/maven.yml)

# How it works

This application basically uses Quarkus Framework with Java 11 with some other modules known to development community:

* Hibernate 5
* Jackson for JSON
* H2 in memory database
* JPA Criteria
* Auth0 java-jwt

### Project structure:

```
application/            -> business orchestration layer
+-- web/                -> web layer models and resources
domain/                 -> core business implementation layer
+-- model/              -> core business entity models
+-- feature/            -> all features logic implementation
+-- validator/          -> model validation implementation 
+-- exception/          -> all business exceptions
infrastructure/         -> technical details layer
+-- configuration/      -> dependency injection configuration
+-- repository/         -> adapters for domain repositories
+-- provider/           -> adapters for domain providers
+-- web/                -> web layer infrastructure models and security
```

# Getting started

### Start local server

```shell
 ./mvnw compile quarkus:dev
 ```

The server should be running at http://localhost:8080

### Running the application tests

```shell
./mvnw test 
```

### Running postman collection tests

```shell
./collections/run-api-tests.sh
```

### Building jar file

```shell
./mvnw package
```

### Building native executable

GraalVM is necessary for building native executable, more information about setting up GraalVM can be found
in [Quarkus guides](https://quarkus.io/guides/)
and database engine need to be changed.

```shell
./mvnw package -Pnative
```

#### Database changes can be made to the application.properties file.

```properties
# Database configuration
quarkus.datasource.db-kind=h2
quarkus.datasource.jdbc.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
quarkus.datasource.jdbc.driver=org.h2.Driver
quarkus.datasource.username=sa
quarkus.datasource.password=
```

## Help

Improvements are welcome, feel free to contribute.