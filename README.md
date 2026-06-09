[![Java CI with Maven in Linux](https://github.com/YOUR_GITHUB_USERNAME/Digital-Certificate-Management-System/actions/workflows/maven.yml/badge.svg)](https://github.com/YOUR_GITHUB_USERNAME/Digital-Certificate-Management-System/actions/workflows/maven.yml)
[![Java CI with Maven, Docker and SonarCloud in Linux](https://github.com/YOUR_GITHUB_USERNAME/Digital-Certificate-Management-System/actions/workflows/sonar.yml/badge.svg)](https://github.com/YOUR_GITHUB_USERNAME/Digital-Certificate-Management-System/actions/workflows/sonar.yml)
[![Coverage Status](https://coveralls.io/repos/github/YOUR_GITHUB_USERNAME/Digital-Certificate-Management-System/badge.svg?branch=master)](https://coveralls.io/github/YOUR_GITHUB_USERNAME/Digital-Certificate-Management-System?branch=master)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=YOUR_SONAR_PROJECT_KEY&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=YOUR_SONAR_PROJECT_KEY)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=YOUR_SONAR_PROJECT_KEY&metric=coverage)](https://sonarcloud.io/summary/new_code?id=YOUR_SONAR_PROJECT_KEY)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=YOUR_SONAR_PROJECT_KEY&metric=bugs)](https://sonarcloud.io/summary/new_code?id=YOUR_SONAR_PROJECT_KEY)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=YOUR_SONAR_PROJECT_KEY&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=YOUR_SONAR_PROJECT_KEY)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=YOUR_SONAR_PROJECT_KEY&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=YOUR_SONAR_PROJECT_KEY)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=YOUR_SONAR_PROJECT_KEY&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=YOUR_SONAR_PROJECT_KEY)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=YOUR_SONAR_PROJECT_KEY&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=YOUR_SONAR_PROJECT_KEY)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=YOUR_SONAR_PROJECT_KEY&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=YOUR_SONAR_PROJECT_KEY)

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
