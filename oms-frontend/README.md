# OMS React Frontend

A responsive React frontend for the Full-Stack Order Management System.

The application provides separate customer and administrator portals secured using JWT authentication and role-based route protection.

## Features

### Authentication

- Customer registration
- Secure login
- JWT stored in browser local storage
- Axios interceptor automatically sends JWT tokens
- Role-based navigation
- Protected customer and administrator routes
- Logout and session clearing

### Customer Portal

- Personalized customer dashboard
- Create new orders
- View only orders belonging to the authenticated customer
- Track current order status
- Total, active, and delivered order summaries
- Responsive order table
- Protection against accessing administrator routes

### Administrator Portal

- Administrator dashboard
- View all customer orders
- Server-side pagination
- Search current-page orders
- Filter orders by status
- Update order status
- Delete orders
- View order counts and order-value summaries
- Protection against accessing customer routes

## Technology Stack

| Technology | Purpose |
|---|---|
| React | Component-based user interface |
| Vite | Development server and production build |
| React Router | Navigation and protected routes |
| Axios | Communication with the Spring Boot REST API |
| ESLint | JavaScript code validation |
| CSS | Responsive dashboard design |

## Project Structure

```text
oms-frontend/
├── public/
│   ├── favicon.svg
│   └── icons.svg
│
├── src/
│   ├── assets/
│   ├── components/
│   │   ├── LogoutButton.jsx
│   │   └── ProtectedRoute.jsx
│   │
│   ├── pages/
│   │   ├── admin/
│   │   │   └── AdminDashboard.jsx
│   │   ├── customer/
│   │   │   ├── CreateOrderPage.jsx
│   │   │   └── CustomerDashboard.jsx
│   │   ├── LoginPage.jsx
│   │   └── RegisterPage.jsx
│   │
│   ├── services/
│   │   └── api.js
│   │
│   ├── App.css
│   ├── App.jsx
│   ├── index.css
│   └── main.jsx
│
├── .env.example
├── eslint.config.js
├── package.json
├── vite.config.js
└── README.md
```

## Application Routes

| Route | Access | Purpose |
|---|---|---|
| `/login` | Public | User and administrator login |
| `/register` | Public | Create a customer account |
| `/customer/dashboard` | USER | View personal orders |
| `/customer/orders/new` | USER | Create a new order |
| `/admin/dashboard` | ADMIN | Manage all customer orders |

## Role Protection

The `ProtectedRoute` component checks the authenticated user's role before displaying a page.

- A `USER` attempting to access `/admin/dashboard` is redirected to the customer dashboard.
- An `ADMIN` attempting to access `/customer/dashboard` is redirected to the administrator dashboard.
- An unauthenticated visitor is redirected to `/login`.

Backend authorization remains the final security layer. Frontend route protection improves navigation but does not replace Spring Security.

## Environment Configuration

Create `.env.local` from `.env.example`:

```env
VITE_API_BASE_URL=http://localhost:8080
```

The `.env.local` file is ignored by Git.

The API client reads the backend URL using:

```javascript
import.meta.env.VITE_API_BASE_URL
```

## Prerequisites

- Node.js
- npm
- Running Spring Boot backend on port `8080`

## Installation

From the frontend directory:

```bash
npm install
```

## Start Development Server

```bash
npm run dev
```

The frontend normally runs at:

```text
http://localhost:5173
```

The Vite terminal must remain running while using the development website.

## Production Build

```bash
npm run build
```

The optimized production files are generated in:

```text
dist/
```

The `dist` folder is excluded from Git.

## Preview Production Build

```bash
npm run preview
```

Vite normally serves the preview at:

```text
http://localhost:4173
```

## Code Validation

```bash
npm run lint
npm run build
```

Validated result:

```text
ESLint passed
136 modules transformed
Production build completed successfully
```

## Backend API

The Spring Boot backend is located at:

```text
../ordermanagement
```

Default backend URL:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

## Security Notes

- Do not store database passwords or JWT signing secrets in frontend files.
- `VITE_` environment variables are visible to browser users.
- Only the backend base URL belongs in `VITE_API_BASE_URL`.
- The JWT signing secret remains exclusively in the Spring Boot environment.
- API permissions are enforced by Spring Security.

## Author

**Harsh Rajvardhan Gupta**