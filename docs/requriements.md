**Project Title:** E-commerce Platform for Online Buying and Selling

**Objective:** To build a scalable and robust e-commerce platform that allows users to create accounts, browse products, add them to a cart, and complete orders. This system is designed for high availability and performance, handling a large user base and significant request volume.

**Functional Requirements:**

1.  **User Account Management (Passwordless Authentication):**
    * Users can create accounts using their email address and phone number.
    * No authentication is required
2.  **Product Catalog & Browse:**
    * Users can view a list of available products.
    * Users can add products to their shopping cart.
3.  **Shopping Cart Management:**
    * Users can add, remove, and update quantities of items in their cart.
    * The cart state should persist across sessions.
4.  **Order Placement & Checkout:**
    * Users can initiate the checkout process from their cart.
    * Users can complete the order without payment or anything else

**Non-Functional Requirements:**

1.  **Scalability:**
    * Support for 10 million concurrent users.
    * Ability to handle 1000 requests per second.
2.  **Concurrency & Data Consistency:**
    * Robust handling of concurrent operations, especially for inventory management.
    * Ensure data consistency across the system.
3.  **High Availability & Fault Tolerance:**
    * The system should be resilient to failures.

**Architectural Considerations & Technology Stack (Open Source Only):**

* **Backend Language:** Kotlin
* **Database:** PostgreSQL (for relational data like user profiles, product details, orders)
* **Caching:** Redis (for session management, frequently accessed product data, hot inventory items)
---

### Detailed Task List (Implementation Focus):

**A. Core Services & APIs (Microservices Architecture Recommended)**

1.  **User Service:**
    * **Data Model:** `User` (id, email, phone_number, creation_timestamp, last_login_timestamp, status)
    * Implement user registration via email/phone number.
    * API Endpoints: `/users/register`
2.  **Product Catalog Service:**
    * **Data Model:** `Product` (id, name, description, price, available_quantity, image_url, category, seller_id)
    * Implement product listing and retrieval.
    * API Endpoints: `/products`, `/products/{id}`
3.  **Shopping Cart Service:**
    * **Data Model:** `Cart` (user_id, product_id, quantity, added_timestamp)
    * Implement add to cart, update cart item quantity, remove from cart.
    * Persistence of cart state (e.g., in Redis for speed, or a dedicated `carts` table in PostgreSQL for durability).
    * API Endpoints: `/cart`, `/cart/add`, `/cart/update`, `/cart/remove`
4.  **Order Service:**
    * **Data Model:** `Order` (id, user_id, order_status, total_amount, order_timestamp, delivery_address)
    * **Data Model:** `OrderItem` (id, order_id, product_id, quantity, price_at_purchase)
    * Implement checkout process:
        * Validate cart items and availability.
        * Create a new order in a pending state.
        * **Concurrency Handling (Inventory):** Implement optimistic locking or a distributed lock mechanism (e.g., Redis locks) to atomically decrement inventory during order creation. If lock contention is high, consider queueing requests via Kafka for sequential processing.
        * Publish an "Order Created" event to Kafka for asynchronous processing (e.g., inventory deduction, notification).
    * API Endpoints: `/orders/checkout`

**B. Infrastructure & Scalability**

1.  **Database Design & Optimization:**
    * Schema definition for User, Product, Cart, Order, OrderItem.
    * Index creation for frequently queried columns (user_id, product_id, order_id, etc.).
    * Consider database sharding/partitioning for extreme scale (for 10M users, this becomes relevant).
2.  **Caching Strategy (Redis):**
    * Implement Redis for session management.
    * Cache frequently accessed product details.
    * Cache hot inventory items to reduce database load.

3.  **Concurrency Management:**
    * **Inventory:**
        * **Approach 1 (Optimistic Locking):** Add a `version` column to the `Product` table. When updating `available_quantity`, check the `version` and increment it. If the `version` doesn't match during an update, retry the operation.
        * **Approach 2 (Distributed Locks with Redis):** Acquire a lock for a specific `product_id` before modifying its quantity. Release the lock after the transaction.
    * **General:** Use thread-safe data structures and synchronization mechanisms where necessary in the application code.



---
IGNORE THIS 
### What this addition achieves:

* **Clarity on "How":** It doesn't just state requirements but suggests concrete technologies and architectural patterns to meet them.
* **Addresses Concurrency Directly:** Provides specific methods for inventory management.
* **High-Level Data Models:** Gives the agent an immediate understanding of the core entities.
* **API Guidance:** Briefly outlines the main API surface.
* **Realistic Scope:** Includes essential non-functional aspects like monitoring and logging.

This detailed breakdown will give the coding agent a much clearer roadmap and allow them to start implementation with a stronger understanding of your vision.


learnings to be used in the future
1. Follow the current project structure
2. Create clients for the new technologies and integrate with the database layer
3. skip the testing of redis and database and controllers and just write test cases for service layer
4. use synchronous library and not reactive for database and other libraries.
5. Defining the libraries to use for clients will help
6. Define the Database layer and its structure