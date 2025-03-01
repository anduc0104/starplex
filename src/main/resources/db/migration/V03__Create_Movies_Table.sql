CREATE TABLE movies (
    id SERIAL PRIMARY KEY,
       title VARCHAR(255) NULL,
       duration FLOAT NULL,
       release_date DATE NULL,
       description TEXT NULL,
       image VARCHAR(255) NULL

);
