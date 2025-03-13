CREATE TABLE movies (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    duration FLOAT,
    release_date DATE,
    description TEXT,
    images VARCHAR(255),
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP
);
