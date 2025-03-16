CREATE TABLE showtimes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    movie_id INTEGER NULL REFERENCES movies(id) ON DELETE CASCADE,
    room_id INTEGER NULL REFERENCES rooms(id) ON DELETE CASCADE,
    show_date DATE NULL,
    show_time TIME NULL,
    price DECIMAL(10, 2) NULL,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (movie_id) REFERENCES movies(id),
    FOREIGN KEY (room_id) REFERENCES rooms(id)
);
