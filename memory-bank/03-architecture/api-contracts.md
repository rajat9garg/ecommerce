# API Contracts

## Base URL
`https://api.ecommerce.example.com/v1`

## Authentication
- **Type**: OAuth 2.0 with JWT
- **Header**: `Authorization: Bearer <token>`
- **Scopes**:
  - `products:read` - Read product information
  - `products:write` - Create/update products
  - `orders:read` - View orders
  - `orders:write` - Create/update orders
  - `users:profile` - Access user profile

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
