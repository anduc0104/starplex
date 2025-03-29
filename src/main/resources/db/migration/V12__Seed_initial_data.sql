-- 1. Insert into users
INSERT INTO users (full_name, username, email, phone, password, role, created_at) VALUES
('Nguyen Van A', 'admin', 'admin@example.com', '0912345671', '$2a$12$Qtiw5reG1xJc.DVdl4kVneibqx2z59o6KZriZGmUWlqJ.daEfd9u2', 'admin', NOW()),
('Nguyen Van B', 'staff', 'staff@example.com', '0912345672', '$2a$12$Qtiw5reG1xJc.DVdl4kVneibqx2z59o6KZriZGmUWlqJ.daEfd9u2', 'staff', NOW())

INSERT INTO movies (id, `title`, `duration`, `release_date`, `description`, `images`) VALUES
(1, 'The Hero Returns', 120, '2025-01-01', 'An epic tale of heroism.','file:/C:/Users/Admin/IdeaProjects/starplex/src/main/resources/images/tải%20xuống.jpg'),
(2, 'Laughing Out Loud', 90, '2025-02-10', 'A hilarious comedy.','file:/C:/Users/Admin/IdeaProjects/starplex/src/main/resources/images/tải%20xuống%20(1).jpg'),
(3, 'Haunted Nights', 110, '2025-03-15', 'A chilling horror story.','file:/C:/Users/Admin/IdeaProjects/starplex/src/main/resources/images/tải%20xuống%20(2).jpg');

-- rooms
INSERT INTO rooms (id, room_number, total_seats) VALUES
(1, 1, 126),
(2, 2, 126),
(3, 3, 126);

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
INSERT INTO movie_movie_genres (movie_id, genre_id) VALUES
(1, 1), (1, 5),
(2, 2), (2, 6),
(3, 4), (3, 7);

-- seat types
INSERT INTO seat_types (id, name, price) VALUES
(1, 'Standard', 100000),
(2, 'Premium', 150000),
(3, 'VIP', 200000);
-- seat
INSERT INTO seats (id, room_id, seat_type_id, `row`, col_number) values
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
INSERT INTO showtimes (id, movie_id, room_id, show_date, show_time) VALUES
(1, 1, 1, CURDATE(), '10:00'), (2, 1, 1, '2025-03-17', '13:00'),
(3, 1, 1, CURDATE(), '16:00'), (4, 1, 1, '2025-03-17', '19:00'),
(5, 1, 1, CURDATE(), '10:00'),(9, 2, 2, CURDATE(), '11:00'),
(10, 2, 2, '2025-03-17', '14:00'),
(11, 2, 2, CURDATE(), '17:00'), (12, 2, 2, '2025-03-17', '20:00'),
(13, 3, 3, CURDATE(), '12:00'), (14, 3, 3, '2025-03-23', '15:00'),
(15, 3, 3, CURDATE(), '18:00'), (16, 3, 3, '2025-03-23', '21:00'),
(17, 1, 2, '2025-03-24', '10:00'), (18, 1, 2, '2025-03-24', '13:00'),
(19, 1, 2, '2025-03-24', '16:00'), (20, 1, 2, '2025-03-24', '19:00'),
(21, 2, 3, '2025-03-25', '11:00'), (22, 2, 3, '2025-03-25', '14:00')


-- bookings
INSERT INTO bookings (id, user_id, showtime_id, total_tickets, total_price) VALUES
(1, 1, 1, 2, 200000),
(2, 1, 2, 4, 400000),
(3, 2, 9, 1, 100000),
(4, 2, 13, 3, 300000);

-- payment
INSERT INTO payments (id, booking_id, payment_method, status) VALUES
(1, 1, 'Credit Card', 'Completed'),
(2, 2, 'PayPal', 'Completed'),
(3, 3, 'Cash', 'Pending'),
(4, 4, 'Credit Card', 'Completed');


-- booking detail
INSERT INTO booking_details (id, booking_id, seat_id, price) VALUES
(1, 1, 1, 100000), (2, 1, 2, 100000),
(3, 2, 15, 100000), (4, 2, 16, 100000), (5, 2, 17, 100000), (6, 2, 18, 100000),
(7, 3, 43, 100000),
(8, 4, 85, 100000), (9, 4, 86, 100000), (10, 4, 87, 100000);

UPDATE seats
SET seat_type_id =
    CASE
        WHEN `row` = 'A' THEN 1
        WHEN `row` = 'B' THEN 1
        WHEN `row` = 'C' THEN 1
        WHEN `row` = 'D' THEN 1
        WHEN `row` = 'E' THEN 2
        WHEN `row` = 'F' THEN 2
        WHEN `row` = 'G' THEN 2
        WHEN `row` = 'H' THEN 1
        WHEN `row` = 'I' THEN 3
        ELSE seat_type_id -- Giữ nguyên nếu không khớp
    END;