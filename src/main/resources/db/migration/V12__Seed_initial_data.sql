-- 1. Insert into users
INSERT INTO users (full_name, username, email, phone, password, role, created_at) VALUES
('Nguyen Van A', 'admin', 'admin@example.com', '0912345671', '$2a$12$Qtiw5reG1xJc.DVdl4kVneibqx2z59o6KZriZGmUWlqJ.daEfd9u2', 'admin', NOW()),
('Nguyen Van B', 'staff', 'staff@example.com', '0912345672', '$2a$12$Qtiw5reG1xJc.DVdl4kVneibqx2z59o6KZriZGmUWlqJ.daEfd9u2', 'staff', NOW())

-- 2. Insert into movies
INSERT INTO movies (id, title, duration, release_date, description, created_at) VALUES
(1, 'Inception', 148, '2010-07-16', 'A mind-bending thriller.', NOW()),
(2, 'Titanic', 195, '1997-12-19', 'A tragic love story.', NOW()),
(3, 'Avatar', 162, '2009-12-18', 'A sci-fi epic.', NOW()),
(4, 'The Dark Knight', 152, '2008-07-18', 'Batman vs Joker.', NOW()),
(5, 'Forrest Gump', 142, '1994-07-06', 'A man with a unique journey.', NOW()),
(6, 'Interstellar', 169, '2014-11-07', 'Space exploration.', NOW()),
(7, 'The Matrix', 136, '1999-03-31', 'Virtual reality revealed.', NOW()),
(8, 'Joker', 122, '2019-10-04', 'Origin story of Joker.', NOW()),
(9, 'Avengers: Endgame', 181, '2019-04-26', 'End of an era.', NOW()),
(10, 'The Godfather', 175, '1972-03-24', 'Mafia crime drama.', NOW());


-- 3. Insert into movie_types
INSERT INTO movie_genres (id, name, created_at) VALUES
(1, 'Action', NOW()),
(2, 'Drama', NOW()),
(3, 'Sci-Fi', NOW()),
(4, 'Romance', NOW()),
(5, 'Comedy', NOW()),
(6, 'Horror', NOW()),
(7, 'Fantasy', NOW()),
(8, 'Thriller', NOW()),
(9, 'Adventure', NOW()),
(10, 'Animation', NOW());

INSERT INTO movie_movie_genres (movie_id, genre_id, created_at) VALUES
(1, 3, NOW()), -- Inception (Sci-Fi)
(2, 4, NOW()), -- Titanic (Romance)
(3, 9, NOW()), -- Avatar (Adventure)
(4, 1, NOW()), -- The Dark Knight (Action)
(5, 2, NOW()), -- Forrest Gump (Drama)
(6, 3, NOW()), -- Interstellar (Sci-Fi)
(7, 3, NOW()), -- The Matrix (Sci-Fi)
(8, 8, NOW()), -- Joker (Thriller)
(9, 1, NOW()), -- Avengers: Endgame (Action)
(10, 2, NOW()); -- The Godfather (Drama)


-- 5. Insert into rooms
INSERT INTO rooms (id, room_number, total_seats, created_at) VALUES
(1, 101, 50, NOW()),
(2, 102, 60, NOW()),
(3, 103, 70, NOW()),
(4, 104, 80, NOW()),
(5, 105, 50, NOW()),
(6, 106, 60, NOW()),
(7, 107, 70, NOW()),
(8, 108, 80, NOW()),
(9, 109, 50, NOW()),
(10, 110, 60, NOW());

-- 6. Insert into seat_types
INSERT INTO seat_types (id, name, price, created_at) VALUES
(1, 'Regular', 5.00, NOW()),
(2, 'VIP', 10.00, NOW()),
(3, 'Premium', 15.00, NOW());

-- 7. Insert into seats
INSERT INTO seats (id, room_id, seat_type_id, seat_number, created_at) VALUES
(1, 1, 1, 'A1', NOW()),
(2, 1, 1, 'A2', NOW()),
(3, 1, 2, 'B1', NOW()),
(4, 2, 1, 'A1', NOW()),
(5, 2, 3, 'C1', NOW()),
(6, 3, 2, 'B2', NOW()),
(7, 4, 3, 'C2', NOW()),
(8, 5, 1, 'A1', NOW()),
(9, 6, 2, 'B1', NOW()),
(10, 7, 3, 'C1', NOW());

-- 8. Insert into showtimes
INSERT INTO showtimes (movie_id, room_id, show_date, show_time, price) VALUES
(1,  1, '2025-03-20', '09:00:00', 75.00),
(1,  1, '2025-03-20', '14:00:00', 90.00),
(1,  2, '2025-03-20', '19:30:00', 120.00),
(2,  3, '2025-03-21', '10:00:00', 85.00),
(2,  3, '2025-03-21', '16:00:00', 100.00),
(3,  4, '2025-03-22', '13:30:00', 95.00),
(3,  4, '2025-03-22', '18:45:00', 110.00),
(4,  2, '2025-03-23', '11:00:00', 80.00),
(4,  5, '2025-03-23', '15:00:00', 95.00),
(5,  1, '2025-03-24', '20:00:00', 125.00),
(5,  6, '2025-03-24', '22:30:00', 130.00),
(6,  3, '2025-03-25', '12:00:00', 88.00),
(6,  7, '2025-03-25', '17:00:00', 105.00),
(7,  8, '2025-03-26', '09:45:00', 78.00),
(7,  2, '2025-03-26', '14:30:00', 98.00),
(8,  9, '2025-03-27', '19:00:00', 115.00),
(8, 10, '2025-03-27', '21:45:00', 128.00),
(9,  5, '2025-03-28', '16:15:00', 99.00),
(9,  6, '2025-03-28', '20:45:00', 118.00),
(10, 7, '2025-03-29', '18:00:00', 102.00);

-- 9. Insert into bookings
INSERT INTO bookings (id, user_id, showtime_id, total_price, status, created_at) VALUES
(1, 1, 1, 10.00, 'Confirmed', NOW()),
(2, 2, 2, 24.00, 'Pending', NOW()),
(3, 3, 3, 15.00, 'Cancelled', NOW());

-- 10. Insert into booking_details
INSERT INTO booking_details (id, booking_id, seat_id, price, created_at) VALUES
(1, 1, 1, 10.00, NOW()),
(2, 2, 2, 12.00, NOW()),
(3, 3, 3, 15.00, NOW());

-- 11. Insert into payments
INSERT INTO payments (id, booking_id, amount, payment_method, status, created_at) VALUES
(1, 1, 10.00, 'Credit Card', 'Completed', NOW()),
(2, 2, 24.00, 'PayPal', 'Pending', NOW()),
(3, 3, 15.00, 'Cash', 'Failed', NOW());
