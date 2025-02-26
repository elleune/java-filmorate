package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository("genreDbStorage")
public class GenreDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> findAll() {
        String sql = "SELECT * FROM genres";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    public Optional<Genre> findById(Long id) {
        String sql = "SELECT * FROM genres WHERE id = ?";
        List<Genre> list = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public List<Genre> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        String placeholders = ids.stream()
                .map(id -> "?")
                .collect(Collectors.joining(","));
        String sql = "SELECT * FROM genres WHERE id IN (" + placeholders + ")";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), ids.toArray());
    }

    public Genre create(Genre genre) {
        String sql = "INSERT INTO genres (name) VALUES (?)";
        jdbcTemplate.update(sql, genre.getName());
        return genre;
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Genre g = new Genre();
        g.setId(rs.getLong("id"));
        g.setName(rs.getString("name"));
        return g;
    }
}