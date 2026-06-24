# Manufacturing Quote Platform

Manufacturing Quote Platform is a Java Spring Boot backend application for custom manufacturing quotation requests. Users will be able to upload CAD files, choose materials and technologies, receive calculated price estimates, approve quotations, and track production orders.

## Stack

- Java 21
- Spring Boot
- Maven
- PostgreSQL
- Spring Data JPA
- Spring Security
- Jakarta Validation
- Swagger / OpenAPI
- Docker Compose

## Run Locally

Start PostgreSQL:

```bash
docker compose up -d
```

Run the application:

```bash
mvn spring-boot:run
```

Useful URLs:

- API base: `http://localhost:8080/api/v1`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Health check: `http://localhost:8080/api/v1/health`

## First Milestone

- Spring Boot Maven project
- PostgreSQL configuration
- Swagger configuration
- Common API response format
- Global exception handler
- Basic health endpoint
