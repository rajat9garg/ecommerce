# Technical Context

**Created**: 2025-05-23  
**Last Updated**: 2025-05-24  
**Status**: [ACTIVE]

## Purpose
This document specifies the technology stack, development setup, and infrastructure configuration for the e-commerce platform.

## Table of Contents
- [Technology Stack](#technology-stack)
- [Development Environment](#development-environment)
- [Prerequisites](#prerequisites)
- [Infrastructure Configuration](#infrastructure-configuration)
- [Deployment](#deployment)
- [Monitoring and Logging](#monitoring-and-logging)
- [Security Considerations](#security-considerations)
- [Scaling Strategy](#scaling-strategy)

## Technology Stack

### Backend
- **Runtime**: Java 21
- **Framework**: Spring Boot 3.4.5
- **Language**: Kotlin 1.9.25
- **Build Tool**: Gradle (Kotlin DSL)
- **API**: RESTful with OpenAPI 3.0
- **Documentation**: OpenAPI Generator 7.5.0

### Database
- **Primary**: PostgreSQL 15
- **Migrations**: Flyway
- **Database Access**: Spring Data JDBC, JOOQ
- **Cache**: Redis 7.0 (Lettuce client)

### DevOps
- **Containerization**: Docker
- **CI/CD**: GitHub Actions
- **Testing**: JUnit 5, MockK, Testcontainers

### DevOps
- **Containerization**: Docker 20.10+
- **Orchestration**: Kubernetes
- **CI/CD**: GitHub Actions
- **Infrastructure as Code**: Terraform
- **Cloud Provider**: AWS

### Monitoring
- **APM**: New Relic
- **Logging**: ELK Stack
- **Metrics**: Prometheus, Grafana

## Development Environment

### Prerequisites
- Java 21 JDK
- Docker and Docker Compose
- Gradle 8.0+
- PostgreSQL 15+
- Redis 7.0+

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-org/ecommerce.git
   cd ecommerce
   ```

2. **Set up environment variables**
   Create a `.env` file in the root directory:
   ```env
   # Database
   SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/ecommerce
   SPRING_DATASOURCE_USERNAME=postgres
   SPRING_DATASOURCE_PASSWORD=postgres
   
   # Redis
   SPRING_REDIS_HOST=localhost
   SPRING_REDIS_PORT=6379
   
   # OpenAPI
   API_BASE_PATH=/api/v1
   ```

3. **Start services**
   ```bash
   # Start database and Redis
   docker-compose up -d
   
   # Build and run the application
   ./gradlew bootRun
   ```

4. **Access the API**
   - API Documentation: http://localhost:8080/swagger-ui.html
   - Health Check: http://localhost:8080/api/v1/health

## Build System

### Key Features
- **Code Generation**:
  - OpenAPI 3.0 code generation
  - JOOQ for type-safe SQL queries
  - Automatic DTO mapping

- **Build Process**:
  - Incremental compilation
  - Test coverage reporting
  - Code quality checks

### Common Tasks
```bash
# Build the project
./gradlew build

# Run tests
./gradlew test

# Generate OpenAPI code
./gradlew openApiGenerate

# Run the application
./gradlew bootRun

# Run with development profile
SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun
```

### Recommended Tools
- IntelliJ IDEA with Kotlin plugin
- Docker Desktop
- Postman or Insomnia for API testing
- DBeaver for database management

## Infrastructure Configuration

### Local Development
- **Database**: PostgreSQL in Docker
- **Cache**: Redis in Docker
- **Ports**:
  - Application: 8080
  - PostgreSQL: 5432
  - Redis: 6379
  - Adminer (DB UI): 8081

### Production (Planned)
- **Container Orchestration**: Kubernetes
- **Database**: Managed PostgreSQL
- **Cache**: Managed Redis
- **CI/CD**: GitHub Actions
- **Monitoring**: Prometheus + Grafana
- **Logging**: ELK Stack

### Networking
- API Gateway for request routing
- Service mesh for inter-service communication
- Rate limiting and circuit breaking

## Deployment

### Environments
1. **Local**
   - For development and testing
   - Uses local Docker containers

2. **Development** (Planned)
   - Branch: `develop`
   - Auto-deployed on push
   - URL: https://dev.ecommerce.example.com

3. **Staging** (Planned)
   - Branch: `staging`
   - Manual deployment
   - Mirrors production environment

4. **Production** (Planned)
   - Branch: `main`
   - Manual deployment with approval
   - URL: https://ecommerce.example.com

### Build Pipeline
1. **On Push to any branch**
   - Lint code
   - Run unit tests
   - Build application
   - Run integration tests

2. **On PR to `develop` or `main`**
   - Run all tests
   - Build Docker image
   - Run security scans
   - Deploy to test environment (if applicable)

## Monitoring and Logging

### Logging
- **Format**: JSON structured logging
- **Levels**: ERROR, WARN, INFO, DEBUG, TRACE
- **Correlation IDs** for request tracing

### Metrics (Planned)
- Application metrics via Micrometer
- JVM metrics
- Custom business metrics
- Prometheus endpoint at `/actuator/prometheus`

### Health Checks
- Spring Boot Actuator health endpoint
- Custom health indicators for:
  - Database connectivity
  - Redis connectivity
  - External service status

## Security

### Authentication & Authorization
- JWT-based authentication (Planned)
- Role-based access control (RBAC)
- OAuth 2.0 integration (Planned)

### Data Protection
- Encryption at rest (AES-256)
- TLS 1.3 for all communications
- Regular security audits
- Dependency vulnerability scanning

### Best Practices
- Input validation
- SQL injection protection
- CSRF protection
- Rate limiting (Planned)
- Secret management with AWS Secrets Manager

## Scaling Strategy
### Horizontal Scaling
- Auto-scaling groups for stateless services
- Read replicas for database
- Sharding for large datasets

### Caching Strategy
- CDN for static assets
- Redis for session storage
- Database query caching
- Edge caching with CloudFront
