-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE,
    phone_number VARCHAR(20) UNIQUE,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_login_at TIMESTAMP WITH TIME ZONE
);

-- Create indexes for faster lookups
CREATE INDEX IF NOT EXISTS idx_users_email ON users (email) WHERE email IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_users_phone_number ON users (phone_number) WHERE phone_number IS NOT NULL;
