CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NULL,
    username VARCHAR(255) UNIQUE NULL,
    email VARCHAR(255) UNIQUE NULL,
    password VARCHAR(255) NULL,
    phone VARCHAR(20) NULL,
    role VARCHAR(50) NULL CHECK (role IN ('admin', 'customer', 'staff')),
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP
);
