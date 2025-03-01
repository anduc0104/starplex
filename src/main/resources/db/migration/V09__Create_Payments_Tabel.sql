CREATE TABLE payments (
    id SERIAL PRIMARY KEY,
    booking_id INTEGER NULL REFERENCES bookings(id) ON DELETE CASCADE,
    amount DECIMAL(10,2) NULL,
    payment_method VARCHAR(50) NULL CHECK (payment_method IN ('Credit Card', 'PayPal', 'Cash')),
    status VARCHAR(50) NULL CHECK (status IN ('Pending', 'Completed', 'Failed')),
    transaction_id VARCHAR(255) NULL,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP
);
