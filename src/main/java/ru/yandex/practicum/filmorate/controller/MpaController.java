package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
public class MpaController {

    private final JdbcTemplate jdbcTemplate;

    public MpaController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public Collection<MPA> findAll() {
        String sql = "select * from RATING";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenres(rs));
    }

    private MPA makeGenres(ResultSet rs) throws SQLException {
        int id = rs.getInt("RATING_ID");
        String name = rs.getString("NAME");
        return MPA.builder().id(id).name(name).build();
    }

    @RequestMapping("/{id}")
    @GetMapping
    public MPA findById(@PathVariable Integer id) {
        String sql = "select * from RATING where RATING_ID=" + id;
        List<MPA> mpas = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenres(rs));
        if (!mpas.isEmpty()) {
            return mpas.getFirst();
        } else {
            throw new ResourceNotFoundException("mpa not found");
        }
    }
}