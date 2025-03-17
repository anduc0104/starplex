CREATE TABLE bookings (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INTEGER NULL REFERENCES users(id) ON DELETE CASCADE,
    showtime_id INTEGER NULL REFERENCES showtimes(id) ON DELETE CASCADE,
    total_tickets INTEGER NULL,
    total_price DECIMAL(10,2) NULL,
    status VARCHAR(50) NULL CHECK (status IN ('Pending', 'Confirmed', 'Cancelled')),
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (showtime_id) REFERENCES showtimes(id)
);
