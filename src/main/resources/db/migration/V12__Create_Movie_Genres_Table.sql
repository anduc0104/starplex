CREATE TABLE movie_genres (
    id SERIAL PRIMARY KEY,
    movie_id INTEGER NULL REFERENCES movies(id) ON DELETE CASCADE,
    movie_type_id INTEGER NULL REFERENCES movie_types(id) ON DELETE CASCADE
);
