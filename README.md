# GitHub Repository Observer

### Overview

This project is a service built with Java, Spring Boot, PostgreSQL, and Kafka. It provides an API to add GitHub repositories for monitoring, manage expiration, and list all observed repositories. A scheduler periodically syncs the repositories by fetching the latest data from the GitHub API, and whenever a repository is synced, a domain event is published using Kafka.

### Prerequisites

Before getting started, make sure you have the following installed:

- **Java 21**
- **Docker and Docker Compose**: needed for spring-boot-docker-compose maven package, which will spin up PostgreSQL and Kafka when the service is started.

### Setup

**Build and run:**:
   ```bash
   ./mvnw clean install -DskipTests=true
   ./mvnw spring-boot:run
   ```

### Testing

**Unit and integration tests can be run using Maven:**
```bash
./mvnw test
```

### API Key Management

The service includes plain API key management to demonstrate functionality:
- The API key is currently read from application properties and can be modified. Default value is `SECFIX-DUMMY-KEY`
- In a real-world scenario, API keys would be securely stored, such as in a secret storage service.

### Configuration

The following properties can be configured in `application-docker.properties`:

- `secfix.api.key`: Specify the value of the dummy API key. Default value is `SECFIX-DUMMY-KEY`.
- `secfix.scheduler.fixedRate.minutes`: The rate of the scheduler in minutes. Default is `1`.
- `secfix.github.auth.token`: GitHub authentication token. Add yours github token here.

### Swagger Documentation

When the service is started, Swagger documentation is available at:
- [Swagger UI](http://localhost:8080/swagger-ui/index.html)

### Kafka UI

Access the Kafka UI at:
- [Kafka UI](http://localhost:8090/ui)


