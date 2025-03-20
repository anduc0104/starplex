CREATE TABLE seats (
    id INT PRIMARY KEY AUTO_INCREMENT,
    room_id INTEGER NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
    seat_type_id INTEGER NOT NULL REFERENCES seat_types(id) ON DELETE CASCADE,
    `row` varchar(1),
    col_number INT,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES rooms(id),
    FOREIGN KEY (seat_type_id) REFERENCES seat_types(id)
);