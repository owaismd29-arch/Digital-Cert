[![Java CI with Maven in Linux](https://github.com/owaismd29-arch/Digital-Cert/actions/workflows/maven.yml/badge.svg)](https://github.com/owaismd29-arch/Digital-Cert/actions/workflows/maven.yml)
[![Java CI with Maven, Docker and SonarCloud in Linux](https://github.com/owaismd29-arch/Digital-Cert/actions/workflows/sonar.yml/badge.svg)](https://github.com/owaismd29-arch/Digital-Cert/actions/workflows/sonar.yml)
[![Coverage Status](https://coveralls.io/repos/github/owaismd29-arch/Digital-Cert/badge.svg?branch=master)](https://coveralls.io/github/owaismd29-arch/Digital-Cert?branch=master)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Digital-Cert&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Digital-Cert)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Digital-Cert&metric=coverage)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Digital-Cert)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Digital-Cert&metric=bugs)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Digital-Cert)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Digital-Cert&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Digital-Cert)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Digital-Cert&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Digital-Cert)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Digital-Cert&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Digital-Cert)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Digital-Cert&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Digital-Cert)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Digital-Cert&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Digital-Cert)
# Digital Certificate Management System

A CRUD-based Digital Certificate Management System built with Java, following the MVC architecture pattern, backed by MongoDB.

## Entity: Certificate

| Field     | Type   | Description                        |
|-----------|--------|------------------------------------|
| id        | String | Unique Certificate ID              |
| title     | String | Certificate Title                  |
| issuedTo  | String | Name of the certificate recipient  |
| issuedBy  | String | Issuing organization               |
| year      | int    | Year of issue                      |

## CRUD Operations

- **Create** — Add a new certificate record
- **Read** — View all certificates / find by ID
- **Update** — Modify an existing certificate record
- **Delete** — Remove a certificate record

## Prerequisites

- Java 8+
- Maven
- Docker

## Run with Docker

```bash
docker-compose up -d
mvn clean package
java -jar target/certmanager-0.0.1-SNAPSHOT.jar
```

## Run Tests

```bash
# Unit Tests + IT + E2E + BDD
mvn verify

# With Coverage
mvn verify -Pjacoco

# Mutation Testing
mvn verify -Pmutation-testing
```
