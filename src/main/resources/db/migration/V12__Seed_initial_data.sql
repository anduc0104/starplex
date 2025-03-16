-- 1. Insert into users
INSERT INTO users (full_name, username, email, phone, password, role, created_at) VALUES
('Nguyen Van A', 'admin', 'admin@example.com', '0912345671', '$2a$12$Qtiw5reG1xJc.DVdl4kVneibqx2z59o6KZriZGmUWlqJ.daEfd9u2', 'admin', NOW()),
('Nguyen Van B', 'staff', 'staff@example.com', '0912345672', '$2a$12$Qtiw5reG1xJc.DVdl4kVneibqx2z59o6KZriZGmUWlqJ.daEfd9u2', 'staff', NOW())

-- 2. Insert into movies
INSERT INTO movies (title, duration, release_date, description, images, directors, actors, created_at)
VALUES
('Inception', 148, '2010-07-16', 'A sci-fi movie about dreams and reality.', '0018386_0.png', 'Christopher Nolan', 'Leonardo DiCaprio', NOW()),
('Avatar', 162, '2009-12-18', 'The story of Pandora and the fight for survival.', '0018386_0.png', 'James Cameron', 'Sam Worthington', NOW()),
('The Dark Knight', 152, '2008-07-18', 'Batman faces Joker in a battle for Gotham.', '0018386_0.png', 'Christopher Nolan', 'Christian Bale', NOW()),
('Interstellar', 169, '2014-11-07', 'Astronauts travel through a wormhole to save humanity.', '0018386_0.png', 'Christopher Nolan', 'Matthew McConaughey', NOW()),
('Titanic', 195, '1997-12-19', 'A tragic love story set on the legendary ship.', '0018386_0.png', 'James Cameron', 'Leonardo DiCaprio', NOW()),
('Avengers: Endgame', 181, '2019-04-26', 'The final battle of the Avengers against Thanos.', '0018386_0.png', 'Anthony Russo, Joe Russo', 'Robert Downey Jr.', NOW()),
('The Matrix', 136, '1999-03-31', 'A hacker discovers the truth about reality.', '0018386_0.png', 'Lana Wachowski, Lilly Wachowski', 'Keanu Reeves', NOW()),
('Jurassic Park', 127, '1993-06-11', 'Dinosaurs are brought back to life in a theme park.', '0018386_0.png', 'Steven Spielberg', 'Sam Neill', NOW()),
('The Godfather', 175, '1972-03-24', 'The story of the Corleone mafia family.', '0018386_0.png', 'Francis Ford Coppola', 'Marlon Brando', NOW()),
('Forrest Gump', 142, '1994-07-06', 'A man with a low IQ changes history through his life journey.', '0018386_0.png', 'Robert Zemeckis', 'Tom Hanks', NOW()),
('Pulp Fiction', 154, '1994-10-14', 'A nonlinear crime story full of twists.', '0018386_0.png', 'Quentin Tarantino', 'John Travolta', NOW()),
('Shawshank Redemption', 142, '1994-09-23', 'A man’s journey to freedom from prison.', '0018386_0.png', 'Frank Darabont', 'Tim Robbins', NOW()),
('Gladiator', 155, '2000-05-05', 'A betrayed general seeks vengeance in the Colosseum.', '0018386_0.png', 'Ridley Scott', 'Russell Crowe', NOW()),
('The Lion King', 118, '1994-06-15', 'A young lion cub embarks on a journey to reclaim his throne.', '0018386_0.png', 'Roger Allers, Rob Minkoff', 'Matthew Broderick', NOW()),
('Fight Club', 139, '1999-10-15', 'An underground fight club takes a dark turn.', '0018386_0.png', 'David Fincher', 'Brad Pitt', NOW()),
('The Lord of the Rings: The Fellowship of the Ring', 178, '2001-12-19', 'A journey to destroy the One Ring.', '0018386_0.png', 'Peter Jackson', 'Elijah Wood', NOW()),
('The Lord of the Rings: The Two Towers', 179, '2002-12-18', 'The battle for Middle-earth intensifies.', '0018386_0.png', 'Peter Jackson', 'Viggo Mortensen', NOW()),
('The Lord of the Rings: The Return of the King', 201, '2003-12-17', 'The final showdown for the fate of Middle-earth.', '0018386_0.png', 'Peter Jackson', 'Ian McKellen', NOW()),
('Harry Potter and the Sorcerer''s Stone', 152, '2001-11-16', 'A young wizard discovers his destiny.', '0018386_0.png', 'Chris Columbus', 'Daniel Radcliffe', NOW()),
('Harry Potter and the Chamber of Secrets', 161, '2002-11-15', 'Hogwarts is threatened by a hidden chamber.', '0018386_0.png', 'Chris Columbus', 'Rupert Grint', NOW());

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
(10, 2, NOW()), -- The Godfather (Drama)
(11, 8, NOW()), -- Pulp Fiction (Thriller)
(12, 2, NOW()), -- Shawshank Redemption (Drama)
(13, 1, NOW()), -- Gladiator (Action)
(14, 10, NOW()), -- The Lion King (Animation)
(15, 8, NOW()), -- Fight Club (Thriller)
(16, 7, NOW()), -- LOTR: The Fellowship of the Ring (Fantasy)
(17, 7, NOW()), -- LOTR: The Two Towers (Fantasy)
(18, 7, NOW()), -- LOTR: The Return of the King (Fantasy)
(19, 7, NOW()), -- Harry Potter and the Sorcerer’s Stone (Fantasy)
(20, 7, NOW()); -- Harry Potter and the Chamber of Secrets (Fantasy)


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
-- Ngày 2025-03-29
(11, 1, '2025-03-29', '08:00:00', 70.00),
(12, 2, '2025-03-29', '09:45:00', 80.00),
(13, 3, '2025-03-29', '11:30:00', 95.00),
(14, 4, '2025-03-29', '13:15:00', 100.00),
(15, 5, '2025-03-29', '15:00:00', 110.00),
(16, 6, '2025-03-29', '16:45:00', 120.00),
(17, 7, '2025-03-29', '18:30:00', 125.00),
(18, 8, '2025-03-29', '20:15:00', 130.00),
(19, 9, '2025-03-29', '22:00:00', 135.00),
(20,10, '2025-03-29', '23:45:00', 140.00),

-- Ngày 2025-03-30
(11, 1, '2025-03-30', '08:30:00', 72.00),
(12, 2, '2025-03-30', '10:15:00', 82.00),
(13, 3, '2025-03-30', '12:00:00', 98.00),
(14, 4, '2025-03-30', '13:45:00', 105.00),
(15, 5, '2025-03-30', '15:30:00', 112.00),
(16, 6, '2025-03-30', '17:15:00', 122.00),
(17, 7, '2025-03-30', '19:00:00', 128.00),
(18, 8, '2025-03-30', '20:45:00', 133.00),
(19, 9, '2025-03-30', '22:30:00', 138.00),
(20,10, '2025-03-30', '23:59:00', 145.00);

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
