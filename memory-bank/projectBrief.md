# Project Brief

**Created**: 2025-05-23  
**Last Updated**: 2025-05-23  
**Status**: [ACTIVE]

## Purpose
This document outlines the technical requirements, architecture, and implementation details for the E-commerce Platform for Online Buying and Selling.

## Table of Contents
- [Project Overview](#project-overview)
- [Technical Stack](#technical-stack)
- [System Architecture](#system-architecture)
- [Core Services](#core-services)
- [Data Models](#data-models)
- [API Endpoints](#api-endpoints)
- [Concurrency Management](#concurrency-management)
- [Infrastructure](#infrastructure)
- [Development Setup](#development-setup)
- [Deployment Strategy](#deployment-strategy)

## Project Overview
The e-commerce platform is designed to be a high-performance, scalable solution with the following key capabilities:
- Passwordless user authentication via email/phone
- Product catalog browsing and search
- Shopping cart management with persistence
- Order processing with inventory management
- High concurrency support (10M users, 1000 RPS)
- Strong data consistency for inventory management

## Technical Stack
- **Backend**: Kotlin with Spring Boot
- **Database**: PostgreSQL (primary data store)
- **Caching**: Redis (sessions, product data, inventory)
- **Message Broker**: Kafka (event streaming)
- **Containerization**: Docker
- **Orchestration**: Kubernetes
- **Monitoring**: Prometheus, Grafana
- **Logging**: ELK Stack
- **CI/CD**: GitHub Actions

## System Architecture
### Microservices
1. **User Service**: Handles user registration and authentication
2. **Product Catalog Service**: Manages product information and search
3. **Shopping Cart Service**: Manages user shopping carts
4. **Order Service**: Processes orders and manages inventory
5. **Notification Service**: Handles email/SMS notifications

### Data Flow
1. User registers via email/phone (User Service)
2. User browses products (Product Catalog Service)
3. User adds items to cart (Shopping Cart Service)
4. User checks out (Order Service)
5. System processes order asynchronously (Kafka events)

## Core Services

### 1. User Service
- **Responsibilities**:
  - User registration with email/phone
  - Session management
  - User profile management
- **Key Features**:
  - Passwordless authentication
  - Session persistence
  - Rate limiting

### 2. Product Catalog Service
- **Responsibilities**:
  - Product CRUD operations
  - Product search and filtering
  - Inventory management
- **Key Features**:
  - Cached product listings
  - Full-text search
  - Category management

### 3. Shopping Cart Service
- **Responsibilities**:
  - Shopping cart management
  - Cart persistence
  - Price calculation
- **Key Features**:
  - Session-based cart
  - Cross-device sync
  - Abandoned cart recovery

### 4. Order Service
- **Responsibilities**:
  - Order processing
  - Inventory management
  - Payment processing
- **Key Features**:
  - Transaction management
  - Inventory consistency
  - Order history

## Data Models

### User
```kotlin
data class User(
    val id: UUID,
    val email: String?,
    val phoneNumber: String?,
    val createdAt: Instant,
    val lastLoginAt: Instant?,
    val status: UserStatus
)
```

### Product
```kotlin
data class Product(
    val id: UUID,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val availableQuantity: Int,
    val imageUrl: String?,
    val category: String,
    val sellerId: UUID
)
```

### Cart
```kotlin
data class Cart(
    val userId: UUID,
    val items: List<CartItem>,
    val createdAt: Instant,
    val updatedAt: Instant
)

data class CartItem(
    val productId: UUID,
    val quantity: Int,
    val addedAt: Instant
)
```

### Order
```kotlin
data class Order(
    val id: UUID,
    val userId: UUID,
    val status: OrderStatus,
    val totalAmount: BigDecimal,
    val items: List<OrderItem>,
    val createdAt: Instant,
    val updatedAt: Instant
)

data class OrderItem(
    val productId: UUID,
    val quantity: Int,
    val priceAtPurchase: BigDecimal
)
```

## API Endpoints

### User Service
- `POST /users/register` - Register a new user
- `GET /users/{userId}` - Get user profile

### Product Catalog Service
- `GET /products` - List all products
- `GET /products/{id}` - Get product details
- `GET /products/search` - Search products

### Shopping Cart Service
- `GET /cart` - Get user's cart
- `POST /cart/items` - Add item to cart
- `PUT /cart/items/{itemId}` - Update cart item
- `DELETE /cart/items/{itemId}` - Remove item from cart

### Order Service
- `POST /orders` - Create new order
- `GET /orders/{orderId}` - Get order details
- `GET /users/{userId}/orders` - List user's orders

## Concurrency Management

### Inventory Management
1. **Optimistic Locking**:
   - Add `version` column to Product table
   - Check version on update
   - Retry on version mismatch

2. **Distributed Locks**:
   - Use Redis for distributed locks
   - Lock per product during inventory updates
   - Automatic lock expiration

### Order Processing
1. **Saga Pattern**:
   - Break order process into transactions
   - Compensating transactions for failures
   - Event-driven state management

2. **Idempotency Keys**:
   - Unique key per order attempt
   - Prevent duplicate processing
   - Ensure exactly-once semantics

## Infrastructure

### Database
- **PostgreSQL**:
  - Primary data store
  - Read replicas for scaling
  - Connection pooling

### Caching
- **Redis**:
  - Session storage
  - Product catalog cache
  - Rate limiting
  - Distributed locks

### Message Broker
- **Kafka**:
  - Order processing events
  - Inventory updates
  - Notifications

### Monitoring
- **Prometheus**: Metrics collection
- **Grafana**: Visualization
- **ELK Stack**: Logging and analysis

## Development Setup

### Prerequisites
- JDK 17+
- Docker & Docker Compose
- PostgreSQL 14+
- Redis 7.0+
- Kafka 3.0+

### Local Development
1. Clone the repository
2. Start dependencies:
   ```bash
   docker-compose up -d postgres redis kafka
   ```
3. Build and run services
4. Access services at http://localhost:8080

## Deployment Strategy

### Environments
1. **Development**:
   - Local Docker Compose
   - Automatic deployments on push

2. **Staging**:
   - Kubernetes cluster
   - Manual deployment
   - Integration testing

3. **Production**:
   - Multi-region Kubernetes
   - Blue/green deployment
   - Canary releases

### Scaling
- **Horizontal Pod Autoscaling**
- **Database Read Replicas**
- **Redis Cluster**
- **Kafka Partitioning**
