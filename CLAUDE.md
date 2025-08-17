# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build System & Commands

This is a multi-module Gradle project using Java 21 and Spring Boot 3.5.0. Key commands:

**Development:**

- `./gradlew build` - Build all modules
- `./gradlew :app:bootRun` - Run the Spring Boot application
- `./gradlew test` - Run unit tests
- `./gradlew :app:integrationTest` - Run integration tests
- `./gradlew cucumber` - Run acceptance tests (Cucumber/Gherkin)
- `./gradlew gatlingRun` - Run performance tests (Gatling)

**Code Quality:**

- `./gradlew check` - Run all checks (tests, checkstyle, spotbugs)
- `./gradlew checkstyleMain checkstyleTest` - Run Checkstyle
- `./gradlew spotbugsMain spotbugsTest` - Run SpotBugs static analysis
- `make lint` - Lint all files (Dockerfiles, pipelines, Terraform)
- `make lint-terraform` - Lint only Terraform files
- `make fix-lint-terraform` - Auto-fix Terraform formatting

**Docker & Local Development:**

- `docker-compose up -d` - Start service dependencies (Firestore, WireMock)
- `docker-compose --profile run-app up` - Start app with dependencies
- `docker-compose --profile observability up` - Start with monitoring stack (Prometheus, Grafana, Jaeger)

## Architecture Overview

**Multi-Module Structure:**

- `app/` - Main Spring Boot application
- `acceptance-test/` - Cucumber BDD tests
- `performance-test/` - Gatling load tests
- `infrastructure/` - Terraform configurations for local and cloud deployment

**Domain-Driven Design:**

The application follows DDD principles with clear separation:

- `domain/` - Core business logic (Task, TaskList, User entities and services)
- `application/rest/` - REST controllers and DTOs
- `application/security/` - Security configuration
- `infrastructure/firestore` - Firestore configuration

**Key Domain Concepts:**

- `TaskList` - Aggregate root containing tasks, owned by a user
- `Task` - Value object with name, description, completion status, tags, deadlines
- Tasks use embedded IDs within TaskLists

**Technology Stack:**

- Spring Boot 3.5.0 with Spring Security, Spring Data MongoDB
- Firestore for persistence
- MapStruct for DTO mapping
- Lombok for boilerplate reduction
- Caffeine for caching
- OpenTelemetry for observability (traces to Jaeger, metrics to Prometheus)

## Testing Strategy

**Test Types:**

- Unit tests in `src/test/` using JUnit 5 and Mockito
- Integration tests in `src/integrationTest/` using @SpringBootTest
- Acceptance tests using Cucumber with REST Assured
- Performance tests using Gatling

**Test Data:**

- Factory classes (TaskFactory, UserFactory) for test data creation
- Integration tests use embedded MongoDB or testcontainers

## Infrastructure

**Local Development:**

- Docker Compose provides Firestore, WireMock, and observability stack
- Terraform modules in `infrastructure/local/` for container orchestration
- Health checks configured for all services

**Cloud Deployment:**

- Terraform configurations for Google Cloud Platform
- Terragrunt for environment management (dev/prod)
- Infrastructure includes VPC, firewall rules, and MongoDB Atlas

## Security & Quality

**Static Analysis:**

- Checkstyle for code style enforcement
- SpotBugs with security plugins (FindSecBugs) for vulnerability detection
- SonarCloud integration for code quality metrics
- Checkov for infrastructure security scanning

**Security Features:**

- Secure Firestore configuration with authentication
