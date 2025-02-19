CREATE TABLE movies (
    id SERIAL PRIMARY KEY,
    movie_type_id INTEGER REFERENCES movie_types(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    duration INTEGER NOT NULL CHECK (duration > 0), -- Thời lượng phim tính bằng phút
    release_date DATE NOT NULL,
    rating VARCHAR(10),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
