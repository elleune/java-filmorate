package ru.yandex.practicum.filmorate.storage.dal.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        List<String> genreIds = resultSet.getString("genre_ids") != null
                ? List.of(resultSet.getString("genre_ids").split(", "))
                : new ArrayList<>();

        List<String> genreNames = resultSet.getString("genre_names") != null
                ? List.of(resultSet.getString("genre_names").split(", "))
                : new ArrayList<>();

        Set<Genre> genres = new HashSet<>();
        IntStream.range(0, genreIds.size()).forEach(i -> genres.add(Genre.builder().id(Long.parseLong(genreIds.get(i))).name(genreNames.get(i)).build()));

        return Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .mpa(Mpa.builder().id(resultSet.getLong("mpa_rating")).name(resultSet.getString("mpa_name")).build())
                .genres(new ArrayList<>(genres))
                .build();
    }
}
