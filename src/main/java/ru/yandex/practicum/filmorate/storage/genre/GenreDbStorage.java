package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        String sql = "SELECT id, name FROM genres";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Genre(rs.getLong("id"), rs.getString("name")));
    }

    @Override
    public Optional<Genre> findById(Long id) {
        String sql = "SELECT id, name FROM genres WHERE id = ?";
        List<Genre> list = jdbcTemplate.query(sql, (rs, rowNum) ->
                new Genre(rs.getLong("id"), rs.getString("name")), id);
        return list.stream().findFirst();
    }

    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        String sql = String.format("SELECT id, name FROM genres WHERE id IN (%s)", inSql);
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        new Genre(rs.getLong("id"), rs.getString("name")),
                ids.toArray()
        );
    }
}
