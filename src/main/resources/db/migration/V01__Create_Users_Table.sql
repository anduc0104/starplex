CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY ,
    full_name VARCHAR(255) NULL,
    username VARCHAR(255) UNIQUE NULL,
    email VARCHAR(255) NULL,
    password VARCHAR(255) NULL,
    phone VARCHAR(20) NULL,
    role VARCHAR(50) NULL CHECK (role IN ('admin', 'staff')),
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP
);
