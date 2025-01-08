package ru.yandex.practicum.filmorate.dao.genry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.constants.ConstantSqlStorage.ALL_FROM_GENRES;

@Slf4j
@Component
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;


    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Genre> getGenreById(long id) {
        log.debug("Получен запрос GET/genres/{id}");
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(ALL_FROM_GENRES + " WHERE PUBLIC.GENRES.GENRE_ID = ?", id);
        if (mpaRows.next()) {
            Genre genre = new Genre(
                    mpaRows.getInt("GENRE_ID"),
                    mpaRows.getString("GENRE_NAME")
            );
            return Optional.of(genre);
        } else {
            log.info("Данные с идентификатором {} не найдены.", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> getAllGenre() {
        log.debug("Получен запрос GET /genres.");
        return jdbcTemplate.query(
                ALL_FROM_GENRES,
                (rs, rowNum) ->
                        new Genre(
                                rs.getInt("GENRE_ID"),
                                rs.getString("GENRE_NAME")
                        )
        );
    }
}
