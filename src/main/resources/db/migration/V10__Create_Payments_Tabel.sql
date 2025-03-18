CREATE TABLE payments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    booking_id INTEGER NULL,
    amount DECIMAL(10,2) NULL,
    payment_method VARCHAR(50) NULL CHECK (payment_method IN ('Credit Card', 'PayPal', 'Cash')),
    status VARCHAR(50) NULL CHECK (status IN ('Pending', 'Completed', 'Failed')),
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE
);
