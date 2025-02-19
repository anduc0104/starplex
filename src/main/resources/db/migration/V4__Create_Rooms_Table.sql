CREATE TABLE rooms (
    id SERIAL PRIMARY KEY,
    room_number INTEGER NOT NULL UNIQUE,
    total_seats INTEGER NOT NULL CHECK (total_seats > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
