-- 1. Insert into users
INSERT INTO users (full_name, username, email, phone, password, role, created_at) VALUES
('Nguyen Van A', 'admin', 'admin@example.com', '0912345671', '$2a$12$Qtiw5reG1xJc.DVdl4kVneibqx2z59o6KZriZGmUWlqJ.daEfd9u2', 'admin', NOW()),
('Nguyen Van B', 'staff', 'staff@example.com', '0912345672', '$2a$12$Qtiw5reG1xJc.DVdl4kVneibqx2z59o6KZriZGmUWlqJ.daEfd9u2', 'staff', NOW())



-- movies
INSERT INTO movies (title, duration, release_date, description, created_at) VALUES
('Inception', 148, '2010-07-16', 'A sci-fi movie about dreams and reality.', NOW()),
('Avatar', 162, '2009-12-18', 'The story of Pandora and the fight for survival.', NOW()),
('The Dark Knight', 152, '2008-07-18', 'Batman faces Joker in a battle for Gotham.', NOW()),
('Interstellar', 169, '2014-11-07', 'Astronauts travel through a wormhole to save humanity.', NOW()),
('Titanic', 195, '1997-12-19', 'A tragic love story set on the legendary ship.', NOW()),
('Avengers: Endgame', 181, '2019-04-26', 'The final battle of the Avengers against Thanos.', NOW()),
('The Matrix', 136, '1999-03-31', 'A hacker discovers the truth about reality.', NOW()),
('Jurassic Park', 127, '1993-06-11', 'Dinosaurs are brought back to life in a theme park.', NOW()),
('The Godfather', 175, '1972-03-24', 'The story of the Corleone mafia family.', NOW()),
('Forrest Gump', 142, '1994-07-06', 'A man with a low IQ changes history through his life journey.', NOW()),
('Pulp Fiction', 154, '1994-10-14', 'A nonlinear crime story full of twists.', NOW()),
('Shawshank Redemption', 142, '1994-09-23', 'A man’s journey to freedom from prison.', NOW()),
('Gladiator', 155, '2000-05-05', 'A betrayed general seeks vengeance in the Colosseum.', NOW()),
('The Lion King', 118, '1994-06-15', 'A young lion cub embarks on a journey to reclaim his throne.', NOW()),
('Fight Club', 139, '1999-10-15', 'An underground fight club takes a dark turn.', NOW()),
('The Lord of the Rings: The Fellowship of the Ring', 178, '2001-12-19', 'A journey to destroy the One Ring.', NOW()),
('The Lord of the Rings: The Two Towers', 179, '2002-12-18', 'The battle for Middle-earth intensifies.', NOW()),
('The Lord of the Rings: The Return of the King', 201, '2003-12-17', 'The final showdown for the fate of Middle-earth.', NOW()),
('Harry Potter and the Sorcerer''s Stone', 152, '2001-11-16', 'A young wizard discovers his destiny.', NOW()),
('Harry Potter and the Chamber of Secrets', 161, '2002-11-15', 'Hogwarts is threatened by a hidden chamber.', NOW());



-- rooms
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


-- movie genres
INSERT INTO movie_genres (id, name) VALUES
(1, 'Action'),
(2, 'Comedy'),
(3, 'Drama'),
(4, 'Horror'),
(5, 'Romance'),
(6, 'Sci-Fi'),
(7, 'Thriller'),
(8, 'Animation');



-- Movie movie genres
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


-- seat types
INSERT INTO seat_types (id, name, price) VALUES
(1, 'Standard', 100000),
(2, 'Premium', 150000),
(3, 'VIP', 200000);


-- seat
INSERT INTO seats (id, room_id, seat_type_id, `row`, col_number) VALUES
(1, 1, 1, 'A', 1), (2, 1, 1, 'A', 2), (3, 1, 1, 'A', 3), (4, 1, 1, 'A', 4),
(5, 1, 1, 'A', 5), (6, 1, 1, 'A', 6), (7, 1, 1, 'A', 7), (8, 1, 1, 'A', 8),
(9, 1, 1, 'A', 9), (10, 1, 1, 'A', 10), (11, 1, 1, 'A', 11), (12, 1, 1, 'A', 12),
(13, 1, 1, 'A', 13), (14, 1, 1, 'A', 14),
(15, 1, 1, 'B', 1), (16, 1, 1, 'B', 2), (17, 1, 1, 'B', 3), (18, 1, 1, 'B', 4),
(19, 1, 1, 'B', 5), (20, 1, 1, 'B', 6), (21, 1, 1, 'B', 7), (22, 1, 1, 'B', 8),
(23, 1, 1, 'B', 9), (24, 1, 1, 'B', 10), (25, 1, 1, 'B', 11), (26, 1, 1, 'B', 12),
(27, 1, 1, 'B', 13), (28, 1, 1, 'B', 14),
(29, 1, 1, 'C', 1), (30, 1, 1, 'C', 2), (31, 1, 1, 'C', 3), (32, 1, 1, 'C', 4),
(33, 1, 1, 'C', 5), (34, 1, 1, 'C', 6), (35, 1, 1, 'C', 7), (36, 1, 1, 'C', 8),
(37, 1, 1, 'C', 9), (38, 1, 1, 'C', 10), (39, 1, 1, 'C', 11), (40, 1, 1, 'C', 12),
(41, 1, 1, 'C', 13), (42, 1, 1, 'C', 14),
(43, 1, 1, 'D', 1), (44, 1, 1, 'D', 2), (45, 1, 1, 'D', 3), (46, 1, 1, 'D', 4),
(47, 1, 1, 'D', 5), (48, 1, 1, 'D', 6), (49, 1, 1, 'D', 7), (50, 1, 1, 'D', 8),
(51, 1, 1, 'D', 9), (52, 1, 1, 'D', 10), (53, 1, 1, 'D', 11), (54, 1, 1, 'D', 12),
(55, 1, 1, 'D', 13), (56, 1, 1, 'D', 14),
(57, 1, 1, 'E', 1), (58, 1, 1, 'E', 2), (59, 1, 1, 'E', 3), (60, 1, 1, 'E', 4),
(61, 1, 1, 'E', 5), (62, 1, 1, 'E', 6), (63, 1, 1, 'E', 7), (64, 1, 1, 'E', 8),
(65, 1, 1, 'E', 9), (66, 1, 1, 'E', 10), (67, 1, 1, 'E', 11), (68, 1, 1, 'E', 12),
(69, 1, 1, 'E', 13), (70, 1, 1, 'E', 14),
(71, 1, 1, 'F', 1), (72, 1, 1, 'F', 2), (73, 1, 1, 'F', 3), (74, 1, 1, 'F', 4),
(75, 1, 1, 'F', 5), (76, 1, 1, 'F', 6), (77, 1, 1, 'F', 7), (78, 1, 1, 'F', 8),
(79, 1, 1, 'F', 9), (80, 1, 1, 'F', 10), (81, 1, 1, 'F', 11), (82, 1, 1, 'F', 12),
(83, 1, 1, 'F', 13), (84, 1, 1, 'F', 14),
(85, 1, 1, 'G', 1), (86, 1, 1, 'G', 2), (87, 1, 1, 'G', 3), (88, 1, 1, 'G', 4),
(89, 1, 1, 'G', 5), (90, 1, 1, 'G', 6), (91, 1, 1, 'G', 7), (92, 1, 1, 'G', 8),
(93, 1, 1, 'G', 9), (94, 1, 1, 'G', 10), (95, 1, 1, 'G', 11), (96, 1, 1, 'G', 12),
(97, 1, 1, 'G', 13), (98, 1, 1, 'G', 14),
(99, 1, 1, 'H', 1), (100, 1, 1, 'H', 2), (101, 1, 1, 'H', 3), (102, 1, 1, 'H', 4),
(103, 1, 1, 'H', 5), (104, 1, 1, 'H', 6), (105, 1, 1, 'H', 7), (106, 1, 1, 'H', 8),
(107, 1, 1, 'H', 9), (108, 1, 1, 'H', 10), (109, 1, 1, 'H', 11), (110, 1, 1, 'H', 12),
(111, 1, 1, 'H', 13), (112, 1, 1, 'H', 14),
(113, 1, 1, 'I', 1), (114, 1, 1, 'I', 2), (115, 1, 1, 'I', 3), (116, 1, 1, 'I', 4),
(117, 1, 1, 'I', 5), (118, 1, 1, 'I', 6), (119, 1, 1, 'I', 7), (120, 1, 1, 'I', 8),
(121, 1, 1, 'I', 9), (122, 1, 1, 'I', 10), (123, 1, 1, 'I', 11), (124, 1, 1, 'I', 12),
(125, 1, 1, 'I', 13), (126, 1, 1, 'I', 14);

-- showtimes
INSERT INTO showtimes (movie_id, room_id, show_date, show_time, price) VALUES
-- Ngày 2025-03-29
(1,  1, '2025-03-29', '08:00:00', 75.00),
(2,  2, '2025-03-29', '09:45:00', 85.00),
(3,  3, '2025-03-29', '11:30:00', 95.00),
(4,  4, '2025-03-29', '13:15:00', 100.00),
(5,  5, '2025-03-29', '15:00:00', 110.00),
(6,  6, '2025-03-29', '16:45:00', 120.00),
(7,  7, '2025-03-29', '18:30:00', 125.00),
(8,  8, '2025-03-29', '20:15:00', 130.00),
(9,  9, '2025-03-29', '22:00:00', 135.00),
(10,10, '2025-03-29', '23:45:00', 140.00),

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

-- bookings
INSERT INTO bookings (id, user_id, showtime_id, total_tickets, total_price) VALUES
(1, 1, 1, 2, 200000),
(2, 1, 2, 4, 400000),
(3, 2, 9, 1, 100000),
(4, 2, 13, 3, 300000);

-- payment
INSERT INTO payments (id, booking_id, payment_method, status, transaction_id) VALUES
(1, 1, 'Credit Card', 'Completed', 'TXN123456'),
(2, 2, 'PayPal', 'Completed', 'TXN123457'),
(3, 3, 'Cash', 'Pending', 'TXN123458'),
(4, 4, 'Credit Card', 'Completed', 'TXN123459'),;


-- booking detail
INSERT INTO booking_details (id, booking_id, seat_id, price) VALUES
(1, 1, 1, 100000), (2, 1, 2, 100000),
(3, 2, 15, 100000), (4, 2, 16, 100000), (5, 2, 17, 100000), (6, 2, 18, 100000),
(7, 3, 43, 100000),
(8, 4, 85, 100000), (9, 4, 86, 100000), (10, 4, 87, 100000);