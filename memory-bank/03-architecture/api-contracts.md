# API Contracts

## Base URL
`https://api.ecommerce.example.com/v1`

## Authentication
- **Type**: Passwordless Authentication
- **Flows**:
  1. **Email/Phone Login**:
     - Request OTP via email/phone
     - Verify OTP to get session token
  2. **Session Management**:
     - `X-Session-Token` header for authenticated requests
     - Token expiration: 30 days
     - Automatic refresh on use

## Error Responses
```json
{
  "error": {
    "code": "error_code",
    "message": "Human-readable error message",
    "details": {
      "field1": "Error detail 1",
      "field2": "Error detail 2"
    },
    "request_id": "req_12345"
  }
}
```

## User Service API

### 1. Request OTP
- **Endpoint**: `POST /auth/otp/request`
- **Request Body**:
  ```json
  {
    "email": "user@example.com",
    "phone": "+1234567890",
    "channel": "email"
  }
  ```
- **Response**:
  ```json
  {
    "success": true,
    "message": "OTP sent successfully",
    "retry_after_seconds": 60
  }
  ```

### 2. Verify OTP and Login
- **Endpoint**: `POST /auth/otp/verify`
- **Request Body**:
  ```json
  {
    "email": "user@example.com",
    "phone": "+1234567890",
    "otp": "123456"
  }
  ```
- **Response**:
  ```json
  {
    "token": "session_token_123",
    "user": {
      "id": "usr_123",
      "email": "user@example.com",
      "phone": "+1234567890",
      "status": "active"
    }
  }
  ```

### 3. Get Current User
- **Endpoint**: `GET /users/me`
- **Headers**:
  - `X-Session-Token: session_token_123`
- **Response**:
  ```json
  {
    "id": "usr_123",
    "email": "user@example.com",
    "phone": "+1234567890",
    "status": "active",
    "created_at": "2023-01-01T00:00:00Z",
    "last_login_at": "2023-06-01T12:00:00Z"
  }
  ```

### 4. Update Profile
- **Endpoint**: `PATCH /users/me`
- **Headers**:
  - `X-Session-Token: session_token_123`
- **Request Body**:
  ```json
  {
    "name": "John Doe",
    "phone": "+1234567890"
  }
  ```
- **Response**: Updated user profile

## Error Responses
```json
{
  "error": {
    "code": "error_code",
    "message": "Human-readable error message",
    "details": {
      "field1": "Error detail 1",
      "field2": "Error detail 2"
    }
  }
}
```

## Product Service API

*Note: Product and Order API contracts remain similar but will use the new authentication scheme*

### 1. Get Product List
- **Endpoint**: `GET /products`
- **Query Params**:
  - `category` (string, optional)
  - `page` (number, default: 1)
  - `limit` (number, default: 20)
  - `sort` (string, enum: [price_asc, price_desc, newest])
- **Response**:
  ```json
  {
    "data": [
      {
        "id": "prod_123",
        "name": "Product Name",
        "price": 99.99,
        "image_url": "https://...",
        "category": "electronics"
      }
    ],
    "meta": {
      "total": 100,
      "page": 1,
      "limit": 20
    }
  }
  ```

### 2. Get Product Details
- **Endpoint**: `GET /products/{id}`
- **Response**:
  ```json
  {
    "id": "prod_123",
    "name": "Product Name",
    "description": "Detailed description...",
    "price": 99.99,
    "images": ["url1", "url2"],
    "attributes": {
      "color": "black",
      "size": "M"
    },
    "in_stock": true,
    "category": "electronics"
  }
  ```

## Order Service API

### 1. Create Order
- **Endpoint**: `POST /orders`
- **Request Body**:
  ```json
  {
    "items": [
      {
        "product_id": "prod_123",
        "quantity": 2
      }
    ],
    "shipping_address": {
      "line1": "123 Main St",
      "city": "San Francisco",
      "state": "CA",
      "zip": "94105",
      "country": "US"
    },
    "payment_method": "credit_card"
  }
  ```
- **Response**:
  ```json
  {
    "order_id": "order_123",
    "status": "processing",
    "total_amount": 199.98,
    "payment_intent": "pi_123"
  }
  ```

## Payment Service API

### 1. Process Payment
- **Endpoint**: `POST /payments/process`
- **Request Body**:
  ```json
  {
    "order_id": "order_123",
    "payment_method_id": "pm_123",
    "amount": 199.98,
    "currency": "USD"
  }
  ```
- **Response**:
  ```json
  {
    "payment_id": "pay_123",
    "status": "succeeded",
    "amount": 199.98,
    "receipt_url": "https://..."
  }
  ```

## Rate Limiting
- **Anonymous**: 100 requests/minute
- **Authenticated**: 1000 requests/minute
- **Admin**: 5000 requests/minute

## Versioning
- **Header**: `Accept: application/vnd.ecommerce.v1+json`
- **URL**: `/v1/...`

## Webhooks
### Order Status Update
- **Event**: `order.updated`
- **Payload**:
  ```json
  {
    "event": "order.updated",
    "data": {
      "order_id": "order_123",
      "new_status": "shipped",
      "timestamp": "2023-01-01T12:00:00Z"
    }
  }
  ```
