# Data Models

## Core Entities

### User
```mermaid
erDiagram
    USER ||--o{ USER_AUTH : has
    USER {
        uuid id PK
        string email
        string phone_number
        string status
        timestamp created_at
        timestamp last_login_at
        timestamp updated_at
    }
    
    USER_AUTH {
        uuid id PK
        uuid user_id FK
        string auth_type
        string auth_id
        json auth_data
        timestamp created_at
    }
```

### 1. Product
```mermaid
erDiagram
    PRODUCT ||--o{ PRODUCT_VARIANT : has
    PRODUCT {
        string id PK
        string name
        string description
        string sku
        decimal price
        string category_id FK
        string status
        timestamp created_at
        timestamp updated_at
    }
    
    PRODUCT_VARIANT {
        string id PK
        string product_id FK
        string sku
        string name
        decimal price_adjustment
        json attributes
    }
```

### 2. Order
```mermaid
erDiagram
    ORDER ||--o{ ORDER_ITEM : contains
    ORDER {
        string id PK
        string user_id FK
        string status
        decimal total_amount
        string shipping_address
        string billing_address
        timestamp created_at
        timestamp updated_at
    }
    
    ORDER_ITEM {
        string id PK
        string order_id FK
        string product_id FK
        int quantity
        decimal unit_price
        decimal total_price
    }
```

## Data Access Patterns

### User Management
1. **User Operations**:
   - Find user by email/phone
   - Check if email/phone exists
   - Get user profile
   - List users with pagination
   - Search users by criteria

### Authentication
1. **Auth Operations**:
   - Create/update auth methods
   - Verify credentials
   - Manage sessions
   - Handle password resets

### Read Patterns
1. **Product Catalog`:
   - Get product by ID
   - List products by category
   - Search products with filters
   - Get related products

2. **Order Management**:
   - Get order by ID
   - List user orders
   - Get order status

### Write Patterns
1. **Product Management**:
   - Create/Update product
   - Update inventory
   - Bulk import products

2. **Order Processing**:
   - Create order
   - Update order status
   - Process payment

## Data Flow
1. **Product Data Flow**:
   ```
   Admin Portal -> Product Service -> Database
   ```

2. **Order Data Flow**:
   ```
   Customer -> API Gateway -> Order Service -> Payment Service -> Order DB
   ```

## Data Validation Rules
1. **Product**:
   - Price must be positive
   - SKU must be unique
   - Required fields: name, price, category

2. **Order**:
   - Total must match sum of items
   - Required shipping/billing info
   - Valid status transitions

## Data Migration Strategy
1. **Versioning**:
   - Schema versioning for backward compatibility
   - Zero-downtime migrations

2. **Backup & Recovery**:
   - Daily backups
   - Point-in-time recovery
   - Test restoration procedures
