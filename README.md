# ![RealWorld Example App](quarkus-logo.png)

> ### Quarkus Framework codebase containing real world examples (CRUD, auth, advanced patterns, etc) that adheres to the [RealWorld](https://github.com/gothinkster/realworld) spec and API.

This codebase was created to demonstrate a fully fledged fullstack application built with [Quarkus](https://quarkus.io/) including CRUD operations, authentication, routing, pagination, and more.

We've gone to great lengths to adhere to the Quarkus community styleguides & best practices.

For more information on how to this works with other frontends/backends, head over to the [RealWorld](https://github.com/gothinkster/realworld) repo.

# How it works

This application basicaly uses Quarkus Framework with Java with some other modules known to development community:

* Java 8
* Hibernate 5
* Jackson for JSON
* H2 in memory database
* JPA Criteria
* Auth0 java-jwt

### Project structure:
```
domain/
+-- builder/
+-- config/
+-- constants/
+-- entity/ 
|   +-- persistent/                 -> only persistent objects
+-- exception/                      -> domain exceptions
+-- repository/                     -> persistent context abstractions
+-- security/                       -> security context abstractions
+-- service                         -> domain bussiness layer

web/
+-- config/                         -> serializer/deserializer singleton options
+-- exception/                      -> web layer exceptions
+-- mapper/                         -> exception handler mapping
+-- model/                          -> request/response models for web layer
|   +-- request/                    -> request model objects
|   +-- response/                   -> response model objects
+-- qualifiers/                     -> qualifiers for dependency injection 
+-- resources/                      -> http routes and their handlers
+-- security/                       -> web layer security implementation
|   +-- annotation/                 -> name binding annotations
|   +-- filter/                     -> filters implementation for check authentication/authorization rules
|   +-- service/                    -> web layer services implementation
+-- validation/                     -> custom validator for request model
```

# Getting started

#### Start local server

```bash
 ./mvnw compile quarkus:dev
 ```
The server should be running at http://localhost:8080

#### Running the application tests

``` 
./mvnw test 
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