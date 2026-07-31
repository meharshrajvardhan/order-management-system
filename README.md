\# Full-Stack Order Management System



A secure full-stack Order Management System built with Java 21, Spring Boot, React, PostgreSQL, JWT authentication, Docker, and Microsoft Azure.



The system provides separate customer and administrator portals with role-based access control. Customers can register, log in, create orders, and track only their own orders. Administrators can view, search, paginate, update, and delete customer orders.



\## Main Features



\### Customer Portal



\- Secure account registration and login

\- Public registration always creates a `USER` account

\- JWT-based authentication

\- Create new orders

\- View only orders belonging to the authenticated customer

\- Track order status

\- Dashboard summary for total, active, and delivered orders

\- Protection against manually accessing administrator routes



\### Administrator Portal



\- Secure administrator login

\- View all customer orders

\- Server-side pagination

\- Search and status filtering

\- Update order status

\- Delete orders

\- Dashboard statistics and order-value summary

\- Protection against manually accessing customer routes



\## Technology Stack



| Layer | Technologies |

|---|---|

| Frontend | React, Vite, React Router, Axios, CSS |

| Backend | Java 21, Spring Boot 3.5.3, Spring Security |

| Authentication | JWT, BCrypt, role-based authorization |

| Database | PostgreSQL 17 |

| Persistence | Spring Data JPA, Hibernate |

| Testing | JUnit 5, Mockito, MockMvc |

| API Documentation | Swagger / OpenAPI |

| Build Tools | Maven, npm |

| Containerization | Docker, Docker Compose |

| Cloud | Microsoft Azure Container Apps, ACR, Azure PostgreSQL |



\## Project Structure



```text

oms/

├── oms-frontend/       # React customer and administrator UI

├── ordermanagement/    # Spring Boot REST API

└── README.md           # Full-stack project documentation



```



\## Application Flow



```text

React Frontend

&#x20;     |

&#x20;     | Axios HTTP requests

&#x20;     | Authorization: Bearer <JWT>

&#x20;     v

Spring Security Filter Chain

&#x20;     |

&#x20;     v

REST Controllers

&#x20;     |

&#x20;     v

Service Layer

&#x20;     |

&#x20;     v

Spring Data JPA

&#x20;     |

&#x20;     v

PostgreSQL

```



\## Security Design



\- Passwords are encrypted using BCrypt.

\- Authentication is stateless.

\- JWT tokens contain the authenticated username and role.

\- Public registration cannot create administrator accounts.

\- Customers can access only their own orders.

\- Administrator operations require the `ADMIN` role.

\- JWT and database secrets are supplied through environment variables.

\- Frontend and backend routes are independently protected.



\## Local Development



\### Prerequisites



\- Java 21

\- Maven

\- PostgreSQL

\- Node.js and npm

\- Git



\### Database Setup



Create the PostgreSQL database:



```sql

CREATE DATABASE order\_management\_db;

```



\### Backend Environment Variables



Configure these variables in the Eclipse Java Application run configuration:



```text

DB\_URL=jdbc:postgresql://localhost:5432/order\_management\_db

DB\_USERNAME=postgres

DB\_PASSWORD=<your-postgresql-password>

JWT\_SECRET=<minimum-32-character-secret>

```



Never commit actual database passwords or JWT secrets.



\### Start the Backend



Run the main class:



```text

ordermanagement.app.App

```



Backend URL:



```text

http://localhost:8080

```



Swagger UI:



```text

http://localhost:8080/swagger-ui/index.html

```



\### Start the Frontend



```bash

cd oms-frontend

npm install

npm run dev

```



Frontend URL:



```text

http://localhost:5173

```



The Vite terminal must remain running during local development.



\## Frontend Environment



Copy:



```text

oms-frontend/.env.example

```



to:



```text

oms-frontend/.env.local

```



Default configuration:



```env

VITE\_API\_BASE\_URL=http://localhost:8080

```



The `.env.local` file is excluded from Git.



\## Validation



\### Frontend



```bash

cd oms-frontend

npm run lint

npm run build

```



\### Backend



```bash

cd ordermanagement

mvn clean test

```



Validated test result:



```text

Tests run: 2, Failures: 0, Errors: 0, Skipped: 0

BUILD SUCCESS

```



\## Role-Based Access



| Operation | USER | ADMIN |

|---|---:|---:|

| Register and log in | Yes | Yes |

| Create an order | Yes | Yes |

| View own orders | Yes | No |

| View all customer orders | No | Yes |

| Update order status | No | Yes |

| Delete orders | No | Yes |

| Access customer dashboard | Yes | No |

| Access administrator dashboard | No | Yes |



\## Deployment



The backend supports Docker and cloud deployment using environment-based configuration. The repository includes:



\- Dockerfile

\- Docker Compose configuration

\- Health endpoint

\- Azure Container Registry documentation

\- Azure Container Apps documentation

\- Azure Database for PostgreSQL integration



Detailed backend and deployment documentation is available in:



```text

ordermanagement/README.md

```



\## Author



\*\*Harsh Rajvardhan Gupta\*\*
