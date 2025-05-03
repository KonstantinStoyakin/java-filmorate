INSERT INTO users (email, login, name, birthday) VALUES
('john@example.com', 'john123', 'John Doe', '1990-05-20'),
('jane@example.com', 'jane456', 'Jane Smith', '1988-11-15'),
('alex@example.com', 'alex789', 'Alex Johnson', '1995-07-10');

INSERT INTO friendships (user_id, friend_id, status) VALUES
(1, 2, 'CONFIRMED'),
(2, 1, 'CONFIRMED'),
(1, 3, 'DELETED');

INSERT INTO mpa (id, name) VALUES
(1, 'G'),
(2, 'PG'),
(3, 'PG-13'),
(4, 'R'),
(5, 'NC-17');

INSERT INTO genres (id, name) VALUES
  (1, 'Комедия'),
  (2, 'Драма'),
  (3, 'Мультфильм'),
  (4, 'Триллер'),
  (5, 'Документальный'),
  (6, 'Боевик');

INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES
('The Matrix', 'A computer hacker learns the truth.', '1999-03-31', 136, 4),
('Inception', 'Dream within a dream.', '2010-07-16', 148, 4),
('Forrest Gump', 'Life is like a box of chocolates.', '1994-07-06', 142, 2);

INSERT INTO film_genres (film_id, genre_id) VALUES
(1, 3), -- The Matrix: Action
(1, 6), -- The Matrix: Sci-Fi
(2, 3), -- Inception: Action
(2, 6), -- Inception: Sci-Fi
(3, 2), -- Forrest Gump: Drama
(3, 5); -- Forrest Gump: Romance

INSERT INTO film_likes (film_id, user_id) VALUES
(1, 1),
(1, 2),
(2, 1),
(3, 3);