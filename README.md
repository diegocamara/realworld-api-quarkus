# ![RealWorld Example App](quarkus-logo.png)

> ### Quarkus Framework codebase containing real world examples (CRUD, auth, advanced patterns, etc) that adheres to the [RealWorld](https://github.com/gothinkster/realworld) spec and API.

This codebase was created to demonstrate a fully fledged fullstack application built with [Quarkus](https://quarkus.io/) including CRUD operations, authentication, routing, pagination, and more.

We've gone to great lengths to adhere to the Quarkus community styleguides & best practices.

For more information on how to this works with other frontends/backends, head over to the [RealWorld](https://github.com/gothinkster/realworld) repo.

[![Build Status](https://travis-ci.org/diegocamara/realworld-api-quarkus.svg?branch=master)](https://travis-ci.org/diegocamara/realworld-api-quarkus)

# How it works

This application basicaly uses Quarkus Framework with Java 8 with some other modules known to development community:

* Hibernate 5
* Jackson for JSON
* H2 in memory database
* JPA Criteria
* Auth0 java-jwt

### Project structure:
```
application/            -> business logic implementation
+--data/                -> data aggregator classes
domain/                     -> core business package
+-- model/
|   +-- builder/
|   +-- constants/
|   +-- entity/             -> only persistent model classes
|   +-- exception/          -> domain exceptions
|   +-- repository/         -> persistent context abstractions
|   +-- provider/           -> providers abstraction (token, hash, slug)
+-- service                 -> domain bussiness abstraction
infrastructure/             -> technical details package
+-- provider/               -> providers implementaion
+-- repository/             -> repository implementation
+-- web/                    -> web layer package
    +-- config/             -> serializer/deserializer singleton options
    +-- exception/          -> web layer exceptions
    +-- mapper/             -> exception handler mapping
    +-- model/              -> request/response models for web layer
    |   +-- request/        -> request model objects
    |   +-- response/       -> response model objects
    +-- qualifiers/         -> qualifiers for dependency injection 
    +-- resources/          -> http routes and their handlers
    +-- security/           -> web layer security implementation
    |   +-- annotation/     -> name binding annotations
    |   +-- context/        -> security context options
    |   +-- filter/         -> filters implementation for check authentication/authorization rules
    |   +-- profile/        -> security profiles options
    +-- validation/         -> custom validations for request model
```

# Getting started

### Start local server

```bash
 ./mvnw compile quarkus:dev
 ```
The server should be running at http://localhost:8080


### Running the application tests

``` 
./mvnw test 
```

### Running postman collection tests

```
./collections/run-api-tests.sh
```

### Building jar file

```
./mvnw package
```

### Building native executable

GraalVM is necessary for building native executable, more information about
setting up GraalVM can be found in [Quarkus guides](https://quarkus.io/guides/)
and database engine need to be changed.

```
./mvnw package -Pnative
```

#### Database changes can be made to the application.properties file.

```properties
# Database configuration
quarkus.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
quarkus.datasource.driver=org.h2.Driver
quarkus.datasource.username=sa
quarkus.datasource.password=
```

## Help
Improvements are welcome, feel free to contribute.