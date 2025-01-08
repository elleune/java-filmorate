package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.constants.ConstantSqlStorage.ALL_FROM_FILMS;

@Slf4j
@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final Validator validator = new Validator();

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = ALL_FROM_FILMS + " F JOIN MPA M ON F.MPA_ID = M.MPA_ID";
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> createFilm(rs));
    }

    @Override
    public Film create(Film film) {
        validator.validationFilm(film);
        String sqlQuery = "insert into FILMS(FILM_ID, FILM_NAME,  RELEASE_DATE, DESCRIPTION, DURATION, MPA_ID) " +
                "values (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                film.getId(),
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getMpa().getId());
        if (film.getGenres().size() != 0) {
            for (Genre genre : film.getGenres()) {
                String sqlQueryGenre = "insert into COMBI_FILMS_GENRES(GENRE_ID, FILM_ID) " +
                        "values (?, ?)";
                jdbcTemplate.update(sqlQueryGenre,
                        genre.getId(),
                        film.getId());
            }
        }
        return film;
    }

    @Override
    public Optional<Film> update(Film film) {
        validator.validationFilm(film);
        List<Genre> genres = film.getGenres().stream().distinct().collect(Collectors.toList());
        String sqlQuery = "UPDATE FILMS SET FILM_ID = ?, FILM_NAME = ?,  RELEASE_DATE =?, " +
                "DESCRIPTION = ?, DURATION =?, MPA_ID=? WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getId(),
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        String sqlQueryDelete = "delete from COMBI_FILMS_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQueryDelete, film.getId());
        for (Genre genre : genres) {
            String sqlQueryGenre = "insert into COMBI_FILMS_GENRES(GENRE_ID, FILM_ID) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQueryGenre,
                    genre.getId(),
                    film.getId());
        }
        return getFilmById(film.getId());
    }

    @Override
    public void delete(long id) {
        String sqlQuery = "delete from FILMS where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
        log.debug("Фильм с id: {} удален", id);
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(ALL_FROM_FILMS + " F " +
                "JOIN MPA M ON F.MPA_ID = M.MPA_ID where FILM_ID = ?", id);
        if (filmRows.next()) {
            Film film = new Film(
                    filmRows.getLong("FILM_ID"),
                    filmRows.getString("FILM_NAME"),
                    filmRows.getDate("RELEASE_DATE").toLocalDate(),
                    filmRows.getString("DESCRIPTION"),
                    filmRows.getLong("DURATION"),
                    new Mpa(filmRows.getInt("MPA_ID"),
                            filmRows.getString("MPA_INFO")));
            addRateToFilm(film);
            return Optional.of(addGenreToFilm(film));
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    public List<Genre> getGenresByFilmId(long id) {
        String sql = "SELECT  DISTINCT COMBI_FILMS_GENRES.GENRE_ID, GENRE_NAME FROM COMBI_FILMS_GENRES " +
                "JOIN GENRES G2 on G2.GENRE_ID = COMBI_FILMS_GENRES.GENRE_ID\n" +
                "                  WHERE COMBI_FILMS_GENRES.FILM_ID = ?" +
                "ORDER BY COMBI_FILMS_GENRES.GENRE_ID ";
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Genre(
                        rs.getInt("GENRE_ID"),
                        rs.getString("GENRE_NAME")), id
        );
    }

    public Film addGenreToFilm(Film film) {
        film.setGenres(getGenresByFilmId(film.getId()));
        return film;
    }

    public void addLike(long filmId, long userId) {
        String sqlQuery = "INSERT INTO LIKES (CLIENT_ID, FILM_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                userId,
                filmId
        );
    }

    public void removeLike(int filmId, int userId) {
        String sqlQuery = "delete from LIKES where CLIENT_ID = ? AND FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return getAllFilms().stream().sorted(Comparator.comparing(Film::getRate).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }


    private Film createFilm(ResultSet rs) throws SQLException {
        Film newFilm = new Film(
                rs.getLong("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getString("DESCRIPTION"),
                rs.getLong("DURATION"),
                new Mpa(rs.getInt("MPA_ID"),
                        rs.getString("MPA_INFO")));
        addGenreToFilm(newFilm);
        addRateToFilm(newFilm);
        return newFilm;
    }

    private void addRateToFilm(Film film) {
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet("Select COUNT(FILM_ID) AS FILM_LIKES FROM LIKES " +
                "WHERE FILM_ID = ?", film.getId());
        if (likeRows.next()) {
            int likesCount = likeRows.getInt("FILM_LIKES");
            film.setRate(likesCount);
        }
    }
}