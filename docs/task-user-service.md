# Task Plan: User Service

## Objective
Implement the User Service for the e-commerce platform, handling user registration and management using passwordless authentication (email/phone number only, no password).

---

## Deliverables
- User data model
- User registration API
- Database schema for users
- Basic input validation
- Unit and integration tests
- API documentation

---

## Steps

### 1. Data Model Design
- Define `User` entity:
  - `id` (UUID or serial)
  - `email` (unique, nullable)
  - `phone_number` (unique, nullable)
  - `creation_timestamp`
  - `last_login_timestamp`
  - `status` (active, inactive, etc.)

### 2. Database Layer
- Create users table in PostgreSQL with relevant constraints and indexes
- Ensure uniqueness for email and phone_number

### 3. Repository & Service Layer
- Implement UserRepository (CRUD operations)
- Implement UserService for registration logic

### 4. API Layer
- Implement `POST /users/register` endpoint
  - Accepts email and/or phone_number
  - Validates input (well-formed email, valid phone format)
  - Creates new user if not existing
  - Returns user details (id, email, phone_number, status)

### 5. Input Validation
- Validate email format (if provided)
- Validate phone number format (if provided)
- Enforce at least one (email or phone) is provided

### 6. Testing
- Write unit tests for UserService
- Write integration tests for registration API

### 7. Documentation
- Document API endpoint and request/response schema
- Add example requests/responses

### 8. Non-Functional
- Ensure endpoint is idempotent (registering same email/phone returns existing user)
- Log registration attempts
- Prepare for future scalability (indexing, sharding readiness)

---

## Notes
- No authentication or login required (per requirements)
- No password storage or handling
- No email/phone verification at this stage
- Follow Kotlin, Spring Boot, and PostgreSQL best practices

---

*Created: 2025-05-22*
*Owner: Engineering Team*
