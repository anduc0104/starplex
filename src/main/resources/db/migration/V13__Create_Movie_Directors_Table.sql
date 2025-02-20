CREATE TABLE movie_directors (
    id SERIAL PRIMARY KEY,
    movie_id INTEGER NULL REFERENCES movies(id) ON DELETE CASCADE,
    director_id INTEGER NULL REFERENCES directors(id) ON DELETE CASCADE
);
