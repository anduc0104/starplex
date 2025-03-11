CREATE TABLE movies (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    directors VARCHAR(255),
    actors VARCHAR(255),
    genres VARCHAR(255),
    duration FLOAT,
    release_date DATE,
    description TEXT,
    images VARCHAR(255)
);
