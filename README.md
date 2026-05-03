# Order Management System (OMS)

Built using Spring Boot that manages users and orders with secure JWT-based authentication.

## Features

* User Registration & Login (JWT आधारित authentication)
* Role-based authorization (ADMIN / USER)
* Order management (CRUD operations)
* Secure REST APIs
* Global exception handling
* Pagination & validation

## Tech Stack

* Java 21
* Spring Boot
* Spring Security
* JWT (JSON Web Token)
* Hibernate / JPA
* PostgreSQL
* Maven

## Authentication

All protected APIs require JWT token:

Authorization: Bearer <your_token>

## API Endpoints

### Auth

* POST /auth/register
* POST /auth/login

### Orders

* POST /orders
* GET /orders
* GET /orders/{id}
* PUT /orders/{id}
* DELETE /orders/{id}

## Database Schema

* Users table
* Orders table (linked via user_id)

##  How to Run

1. Clone the repository
2. Configure PostgreSQL in application.properties
3. Run the application
4. Test APIs using Postman

## Future Enhancements

* Microservices architecture
* Docker containerization
* API Gateway
* Frontend integration (React)

## Author

Harsh Rajvardhan
