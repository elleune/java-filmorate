package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;

@Slf4j
@Repository
public class JdbcGenreRepository extends BaseRepository<Genre> implements GenreStorage {

    public JdbcGenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    private static final String FIND_ALL_GENRE_QUERY = "SELECT * from genres";
    private static final String FIND_NAME_BY_ID_GENRE_QUERY = "SELECT * FROM genres WHERE genre_id = ?";
    private static final String FIND_All_GENRES_FOR_FILM_QUERY = """
            SELECT g.*
            FROM genres g
            INNER JOIN films_genres fg ON g.genre_id = fg.genre_id
            WHERE film_id = ?
            """;

    @Override
    public List<Genre> getAllGenresByFilmId(long filmId) {

        return findMany(FIND_All_GENRES_FOR_FILM_QUERY, filmId);
    }


    @Override
    public Genre getGenreById(int id) {
        Genre genre = findOne(FIND_NAME_BY_ID_GENRE_QUERY, id).orElseThrow(() -> {
            String errorMessage = "Жанра с ID - " + id + " не существует.";
            return new NotFoundException(errorMessage);
        });
        return genre;
    }

    @Override
    public List<Genre> getAllGenres() {
        return findMany(FIND_ALL_GENRE_QUERY);
    }
}
