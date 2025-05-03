package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.UnableToFindException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
@Qualifier("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public Film addFilm(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setLong(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        log.info("Добавлен фильм в базу: {}", film);

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                String insertGenreSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
                jdbcTemplate.update(insertGenreSql, film.getId(), genre.getId());
            }
        }

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film.getId() == null || film.getId() < 1) {
            throw new UnableToFindException("Некорректный ID фильма: " + film.getId());
        }

        Optional<Film> existingFilm = findById(film.getId());
        if (existingFilm.isEmpty()) {
            throw new ResourceNotFoundException("Фильм с id " + film.getId() + " не найден");
        }

        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    @Override
    public Optional<Film> findById(Long id) {
        String sql = "SELECT * FROM films WHERE id = ?";
        return jdbcTemplate.query(sql, this::mapRowToFilm, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Film> getMostPopular(Integer count) {
        String sql =
                "SELECT f.id, f.name, f.description, f.release_date, f.duration, " +
                        "f.mpa_id, m.name AS mpa_name " +
                        "FROM films f " +
                        "LEFT JOIN film_likes fl ON f.id = fl.film_id " +
                        "LEFT JOIN mpa m ON f.mpa_id = m.id " +
                        "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.name " +
                        "ORDER BY COUNT(fl.user_id) DESC " +
                        "LIMIT ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            long filmId = rs.getLong("id");  // Исправлено: было "film_id"

            Set<Long> likes = new HashSet<>(jdbcTemplate.queryForList(
                    "SELECT user_id FROM film_likes WHERE film_id = ?", Long.class, filmId));

            List<Genre> genreList = jdbcTemplate.query(
                    "SELECT g.id, g.name FROM genres g " +
                            "JOIN film_genres fg ON g.id = fg.genre_id " +
                            "WHERE fg.film_id = ?",
                    (rs2, rn2) -> new Genre(rs2.getLong("id"), rs2.getString("name")),
                    filmId
            );

            return Film.builder()
                    .id(filmId)
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .releaseDate(rs.getDate("release_date").toLocalDate())
                    .duration(rs.getInt("duration"))
                    .mpa(new Mpa(rs.getLong("mpa_id"), rs.getString("mpa_name")))
                    .likes(likes)
                    .genres(new HashSet<>(genreList))
                    .build();
        }, count);
    }

    @Override
    public List<Film> findAll() {
        String sql = "SELECT * FROM films";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        long filmId = rs.getLong("id");

        Set<Long> likes = new HashSet<>(jdbcTemplate.queryForList(
                "SELECT user_id FROM film_likes WHERE film_id = ?", Long.class, filmId));

        Mpa mpa = jdbcTemplate.queryForObject(
                "SELECT id, name FROM mpa WHERE id = ?",
                (rs2, rn2) -> new Mpa(rs2.getLong("id"), rs2.getString("name")),
                rs.getLong("mpa_id")
        );

        List<Genre> genreList = jdbcTemplate.query(
                "SELECT g.id, g.name FROM genres g " +
                        "JOIN film_genres fg ON g.id = fg.genre_id " +
                        "WHERE fg.film_id = ?",
                (rs2, rn2) -> new Genre(rs2.getLong("id"), rs2.getString("name")),
                filmId
        );
        Set<Genre> genres = new HashSet<>(genreList);

        return Film.builder()
                .id(filmId)
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(mpa)
                .genres(genres)
                .likes(likes)
                .build();
    }
}

