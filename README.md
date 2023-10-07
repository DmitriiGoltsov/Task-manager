### Hexlet tests and linter status:
[![Actions Status](https://github.com/DmitriiGoltsov/java-project-73/workflows/hexlet-check/badge.svg)](https://github.com/DmitriiGoltsov/java-project-73/actions)

### CodeClimate maintainability and Code coverage badges:
[![Maintainability](https://api.codeclimate.com/v1/badges/995ece44c4d04e10c42d/maintainability)](https://codeclimate.com/github/DmitriiGoltsov/java-project-73/maintainability)

<a href="https://codeclimate.com/github/DmitriiGoltsov/java-project-73/test_coverage"><img src="https://api.codeclimate.com/v1/badges/995ece44c4d04e10c42d/test_coverage" /></a>

## Description:

Task manager application written with Spring Boot, Spring Security and some other technologies (see a list of used technologies below).

The application provides to register new users and, for authenticated users only, to create new:

1) Tasks: main entities of the app. They contain name, description, different unique labels and statuses. Task always shows who its creator is and can also show its appointed executor;
2) Labels: unique entities that can be added to any tasks to help picking them out. For example, a user can create such labels as "bag", "urgent", "design" etc. Those labels help an executor to better organise his/her work;
3) Task statuses: entities that help to organise and control task execution through all stages of workflow process.

The app also supports task filtration. 

## Requirements: 

* JDK 20
* Gradle 8.3 (or above)
* GNU Make

## The main technologies used in the app:

* Spring Boot, Spring Data JPA and Spring Security;
* JSON Web Tokens (JJWT);
* PostgreSQL and H2 Database;
* Liquibase (for database migrations);
* SpringDoc OpenAPI;
* Hibernate (primarily for validation and as a part of Spring Data JPA);
* Lombok for removing some parts of boilerplate code;
* Rollbar error tracking service;
* MockWebServer
* Dependencies are managed by Gradle (Groovy);
* Others

Attention: fronted part of the project was provided by [Hexlet](https://ru.hexlet.io).

## Run the app

``` zsh
make test
```

Run with development settings
``` zsh
make start
```

Run production version
```zsh
make start-prod
```
Default url: http://localhost:7070/login

## APIs:

APIs can be seen via: http://localhost:7070/swagger-ui/index.html