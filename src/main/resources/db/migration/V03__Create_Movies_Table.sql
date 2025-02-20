CREATE TABLE movies (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NULL,
    duration FLOAT NULL,
    release_date DATE NULL,
    rating VARCHAR(10) NULL,
    description TEXT NULL,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP
);
