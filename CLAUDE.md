# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Afterburner is a performance testing demo application built with Spring Boot. It provides various endpoints that simulate common performance scenarios (CPU load, memory usage, delays, remote calls, database operations) for load testing, monitoring, and tuning exercises.

## Build Commands

```bash
# Build the entire project (from root)
./mvnw clean package

# Build and skip tests
./mvnw clean package -DskipTests

# Run the Spring Boot application (from afterburner-java directory)
../mvnw spring-boot:run

# Run with a specific profile
../mvnw spring-boot:run -Dspring-boot.run.profiles=employee-db

# Run tests
./mvnw test

# Run a single test class
./mvnw test -pl afterburner-java -Dtest=MatrixCalculatorTest

# Build Docker image locally (from afterburner-java directory, choose amd64 or arm64)
../mvnw -Plocal-docker -Ddocker-arch=amd64 clean package jib:dockerBuild
```

## Load Testing

```bash
# Run Gatling load test (from afterburner-loadtest-gatling directory)
mvn events-gatling:test

# Run JMeter load test (from afterburner-loadtest-jmeter directory)
mvn clean verify
```

## Architecture

### Module Structure

- **afterburner-java**: Main Spring Boot application with performance test endpoints
- **afterburner-loadtest-gatling**: Gatling load test scripts (Kotlin)
- **afterburner-loadtest-jmeter**: JMeter load test configurations
- **afterburner-wiremock**: WireMock stub server configurations
- **cicd/**: Additional load test configurations (k6, locust, neoload, gatling, jmeter)
- **docker/**: Docker Compose setup for running with dependencies (MariaDB, Prometheus, Jaeger)

### Main Application (afterburner-java)

Package: `io.perfana.afterburner`

Key components:
- **controller/**: REST endpoints for various performance scenarios (CpuBurner, MemoryLeak, MemoryChurn, Delay, RemoteCallController, DatabaseConnector, TcpConnector)
- **client/**: HTTP clients (RemoteCallHttpClient with Apache HttpClient, RemoteCallOkHttp with OkHttp)
- **matrix/**: CPU-intensive matrix calculations for load generation
- **basket/**: Shopping basket domain with validation (demonstrates concurrency testing)
- **mybatis/**: MyBatis mappers for employee database queries
- **service/**: Background services (AutonomousWorker with @Scheduled, AfterburnerRemote for async calls)
- **config/**: Spring configurations (async executor, security, datasources)

### Spring Profiles

- `employee-db`: Activates the employee database controller (requires MariaDB with employee test database)
- `logstash`: Activates logstash logging appender

### Key Configuration Properties

- `afterburner.remote.call.base_url`: Base URL for remote HTTP calls (default: http://localhost:8080)
- `featureToggleIdentityMatrix`: Environment property to enable matrix calculation optimization

## Technology Stack

- Java 8+ (targets JDK 21 for Docker)
- Spring Boot 2.7.x
- Spring Cloud Sleuth for distributed tracing
- Resilience4j for circuit breaker and retry patterns
- Micrometer with Prometheus registry for metrics
- MyBatis for employee database queries
- H2 (in-memory) for basket storage, MariaDB for employee database
