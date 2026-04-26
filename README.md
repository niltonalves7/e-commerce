# 🛒 E-commerce API

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue)
![Docker](https://img.shields.io/badge/Docker-blue)
![Stripe](https://img.shields.io/badge/Stripe-Payment-blueviolet)

REST API for a complete e-commerce platform built with Java and Spring Boot, featuring JWT authentication, product management, shopping cart, order processing and Stripe payment integration.

---

## 🚀 Technologies

- **Java 21**
- **Spring Boot 4**
- **Spring Security** — JWT Authentication
- **PostgreSQL** — Relational database
- **Flyway** — Database migrations
- **Docker & Docker Compose** — Containerization
- **Stripe** — Payment processing
- **MapStruct** — Object mapping
- **Lombok** — Boilerplate reduction
- **Springdoc OpenAPI** — API documentation

---

## 📐 Architecture

The project follows a **layered architecture organized by domain**:

```
src/main/java/com/project/ecommerce/
│
├── domain/          # Business logic
│   ├── auth/        # Authentication (login, register)
│   ├── user/        # User management
│   ├── category/    # Product categories
│   ├── product/     # Products
│   ├── cart/        # Shopping cart
│   ├── order/       # Orders
│   └── payment/     # Payments
│
├── infra/           # Technical concerns
│   ├── config/      # App configurations (CORS, Swagger)
│   ├── exception/   # Global exception handling
│   ├── security/    # JWT, filters, Spring Security
│   └── stripe/      # Stripe webhook handler
│
└── shared/          # Shared resources
    └── enums/       # OrderStatus, PaymentStatus, Role
```

---

## ✅ Features

- **Authentication** — Register and login with JWT
- **Role-based access control** — ADMIN and USER roles
- **Products** — Full CRUD with category filtering and pagination
- **Categories** — Admin-managed product categories
- **Shopping Cart** — Add, update, remove items with stock validation
- **Orders** — Checkout flow converting cart to order
- **Payments** — Stripe PaymentIntent integration with webhook support
- **Order Status** — Automated status transitions via Stripe webhooks
- **API Documentation** — Swagger UI with JWT support

---

## 🔄 Payment Flow

```
1. POST /orders/checkout       → Creates order from cart
2. POST /payments/{orderId}    → Creates Stripe PaymentIntent
3. Frontend confirms payment   → Stripe processes card
4. POST /payments/webhook      → Stripe notifies backend
5. Order status → PROCESSING   → Payment status → PAID
```

---

## 🛠️ Getting Started

### Prerequisites

- Docker & Docker Compose
- Stripe account (for payment testing)

### Setup

**1. Clone the repository**
```bash
git clone https://github.com/niltonalves7/e-commerce.git
cd e-commerce
```

**2. Create `.env` file**
```env
DB_URL=jdbc:postgresql://postgres:5432/ecommerce
DB_USERNAME=admin
DB_PASSWORD=admin
JWT_SECRET=your_jwt_secret
STRIPE_SECRET_KEY=sk_test_...
STRIPE_PUBLISHABLE_KEY=pk_test_...
STRIPE_WEBHOOK_SECRET_KEY=whsec_...
CORS_ALLOWED_ORIGINS=your_frontend_url
```

**3. Build and run**
```bash
./mvnw clean package -DskipTests
docker-compose up --build
```

**4. Access Swagger UI**
```
http://localhost:8080/swagger-ui.html
```

### Stripe Webhook (local testing)
```bash
stripe listen --forward-to localhost:8080/payments/webhook
stripe payment_intents confirm pi_... --payment-method=pm_card_visa
```

---

## 👨‍💻 Author

**Nilton Alves**
- GitHub: [@niltonalves7](https://github.com/niltonalves7)
- LinkedIn: [linkedin.com/in/niltonalves7](https://linkedin.com/in/niltonalves7)