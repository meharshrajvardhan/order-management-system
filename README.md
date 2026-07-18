
# Order Management System

A backend application built using **Java 21** and **Spring Boot** for managing users and customer orders. The application provides secure JWT-based authentication, role-based authorization, and RESTful APIs. It also includes Docker support, API documentation, pagination, sorting, validation, and automated testing.

---

## Features

- User registration and login using JWT authentication
- Role-based authorization (ADMIN / USER)
- CRUD operations for order management
- RESTful API design
- Pagination and sorting
- Request validation
- Global exception handling
- Swagger (OpenAPI) documentation
- Docker and Docker Compose support
- Unit testing using JUnit 5 and Mockito
- Integration testing using MockMvc

---

## Technology Stack

| Category | Technology |
|----------|------------|
| Language | Java 21 |
| Framework | Spring Boot 3.x |
| Security | Spring Security, JWT |
| Database | PostgreSQL |
| ORM | Hibernate, Spring Data JPA |
| Documentation | Swagger (OpenAPI) |
| Testing | JUnit 5, Mockito, MockMvc |
| Containerization | Docker, Docker Compose |
| Build Tool | Maven |
| Version Control | Git, GitHub |

---

# System Architecture

<p align="center">
<img src="docs/architecture.png" width="900">
</p>

### Architecture Flow

```text
Client (Browser/Postman)
            │
            ▼
Spring Boot REST API
            │
            ▼
Spring Security
            │
            ▼
JWT Authentication
            │
            ▼
Business Service Layer
            │
            ▼
Spring Data JPA (Hibernate)
            │
            ▼
PostgreSQL Database
```

---

# Docker Architecture

```text
                 Docker Host
┌───────────────────────────────────────────┐
│                                           │
│        Docker Compose Network             │
│                                           │
│  ┌──────────────────────┐                 │
│  │      oms-app         │                 │
│  │----------------------│                 │
│  │ Spring Boot API      │                 │
│  │ Port : 8080          │                 │
│  └──────────┬───────────┘                 │
│             │                             │
│             │ JDBC                        │
│             ▼                             │
│  ┌──────────────────────┐                 │
│  │    oms-postgres      │                 │
│  │----------------------│                 │
│  │ PostgreSQL 17        │                 │
│  │ Port : 5432          │                 │
│  └──────────────────────┘                 │
│                                           │
└───────────────────────────────────────────┘
```

---

## Project Structure

```text
ordermanagement
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── ordermanagement
│   │   │       ├── app
│   │   │       ├── authcontroller
│   │   │       ├── config
│   │   │       ├── entity
│   │   │       ├── exception
│   │   │       ├── ordercontroller
│   │   │       ├── orderdto
│   │   │       ├── orderservice
│   │   │       ├── repository
│   │   │       ├── security
│   │   │       └── util
│   │   │
│   │   └── resources
│   │       └── application.properties
│   │
│   └── test
│       └── java
│           └── ordermanagement
│               ├── ordercontroller
│               └── orderservice
│
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── README.md
└── .gitignore
```

---

## Package Overview

| Package | Description |
|---------|-------------|
| authcontroller | User registration and authentication |
| ordercontroller | Order REST APIs |
| orderservice | Business logic |
| repository | Database operations |
| entity | Entity classes |
| orderdto | Request & Response DTOs |
| security | JWT & Spring Security configuration |
| config | OpenAPI configuration |
| exception | Global exception handling |
| test | Unit & Integration tests |

---

## REST API Endpoints

### Authentication

| Method | Endpoint |
|----------|----------------|
| POST | `/auth/register` |
| POST | `/auth/login` |

### Orders

| Method | Endpoint |
|----------|----------------|
| POST | `/api/orders` |
| GET | `/api/orders` |
| GET | `/api/orders/{id}` |
| PUT | `/api/orders/{id}` |
| DELETE | `/api/orders/{id}` |

---

# Swagger UI

<p align="center">
<img src="docs/swagger-ui.png" width="100%">
</p>

Interactive API documentation is available after starting the application.

```
http://localhost:8080/swagger-ui/index.html
```

---

# Running the Application

Clone the repository

```bash
git clone https://github.com/meharshrajvardhan/order-management-system.git
cd order-management-system
```

Build

```bash
mvn clean package
```

Run with Docker

```bash
docker compose up -d
```

Verify containers

```bash
docker compose ps
```

Run Tests

```bash
mvn test
```

---

## Future Enhancements

- Deploy to Azure App Service
- Azure Database for PostgreSQL
- GitHub Actions (CI/CD)
- Azure Container Registry
- Redis Caching
- React Frontend
- Microservices Architecture

---

## Author

**Harsh Rajvardhan**

- GitHub: https://github.com/meharshrajvardhan
- Portfolio: https://harshstackdev.me
- LinkedIn: https://www.linkedin.com/in/harshrajvardhangupta/
