INSERT INTO mpa (id, name)
SELECT 1, 'G' WHERE NOT EXISTS (SELECT 1 FROM mpa WHERE id = 1);
INSERT INTO mpa (id, name)
SELECT 2, 'PG' WHERE NOT EXISTS (SELECT 1 FROM mpa WHERE id = 2);
INSERT INTO mpa (id, name)
SELECT 3, 'PG-13' WHERE NOT EXISTS (SELECT 1 FROM mpa WHERE id = 3);
INSERT INTO mpa (id, name)
SELECT 4, 'R' WHERE NOT EXISTS (SELECT 1 FROM mpa WHERE id = 4);
INSERT INTO mpa (id, name)
SELECT 5, 'NC-17' WHERE NOT EXISTS (SELECT 1 FROM mpa WHERE id = 5);

INSERT INTO genres (id, name)
SELECT 1, 'Комедия' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE id = 1);
INSERT INTO genres (id, name)
SELECT 2, 'Драма' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE id = 2);
INSERT INTO genres (id, name)
SELECT 3, 'Мультфильм' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE id = 3);
INSERT INTO genres (id, name)
SELECT 4, 'Триллер' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE id = 4);
INSERT INTO genres (id, name)
SELECT 5, 'Документальный' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE id = 5);
INSERT INTO genres (id, name)
SELECT 6, 'Боевик' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE id = 6);


--INSERT INTO users (email, login, name, birthday)
--SELECT * FROM (VALUES
 -- ('john@example.com', 'john123', 'John Doe', '1990-05-20'),
--  ('jane@example.com', 'jane456', 'Jane Smith', '1988-11-15'),
--  ('alex@example.com', 'alex789', 'Alex Johnson', '1995-07-10')
--) AS vals(email, login, name, birthday)
--WHERE NOT EXISTS (
--  SELECT 1 FROM users WHERE users.email = vals.email
--);

--INSERT INTO films (name, description, release_date, duration, mpa_id)
--SELECT * FROM (VALUES
--  ('The Matrix', 'A computer hacker learns the truth.', '1999-03-31', 136, 4),
--  ('Inception', 'Dream within a dream.', '2010-07-16', 148, 4),
 -- ('Forrest Gump', 'Life is like a box of chocolates.', '1994-07-06', 142, 2)
--) AS vals(name, description, release_date, duration, mpa_id)
--WHERE NOT EXISTS (
 -- SELECT 1 FROM films WHERE films.name = vals.name
--);

--INSERT INTO film_genres (film_id, genre_id)
--SELECT * FROM (VALUES
--  (1, 6), -- Matrix: боевик
--  (2, 6), -- Inception: боевик
--  (3, 2)  -- Forrest Gump: драма
--) AS vals(film_id, genre_id)
--WHERE NOT EXISTS (
--  SELECT 1 FROM film_genres WHERE film_genres.film_id = vals.film_id AND film_genres.genre_id = vals.genre_id
--);

--INSERT INTO film_likes (film_id, user_id)
--SELECT * FROM (VALUES
--  (1, 1),
--  (1, 2),
--  (2, 1),
-- (3, 3)
--) AS vals(film_id, user_id)
--WHERE NOT EXISTS (
--  SELECT 1 FROM film_likes WHERE film_likes.film_id = vals.film_id AND film_likes.user_id = vals.user_id
--);

--INSERT INTO friendships (user_id, friend_id, status)
--SELECT * FROM (VALUES
 -- (1, 2, 'CONFIRMED'),
 -- (2, 1, 'CONFIRMED'),
 -- (1, 3, 'DELETED')
--) AS vals(user_id, friend_id, status)
--WHERE NOT EXISTS (
 -- SELECT 1 FROM friendships WHERE friendships.user_id = vals.user_id AND friendships.friend_id = vals.friend_id
--);