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
INSERT INTO showtimes (id, movie_id, room_id, start_time, price, created_at) VALUES
(1, 1, 1, NOW(), 10.00, NOW()),
(2, 2, 2, NOW(), 12.00, NOW()),
(3, 3, 3, NOW(), 15.00, NOW()),
(4, 4, 4, NOW(), 10.00, NOW()),
(5, 5, 5, NOW(), 8.00, NOW()),
(6, 6, 6, NOW(), 9.00, NOW()),
(7, 7, 7, NOW(), 14.00, NOW()),
(8, 8, 8, NOW(), 11.00, NOW()),
(9, 9, 9, NOW(), 16.00, NOW()),
(10, 10, 10, NOW(), 18.00, NOW());

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
