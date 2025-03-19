CREATE TABLE movie_movie_genres (
    movie_id INT,
    genre_id INT,
    PRIMARY KEY (movie_id, genre_id),
    FOREIGN KEY (movie_id) REFERENCES movies(id),
    FOREIGN KEY (genre_id) REFERENCES movie_genres(id),
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP
);
