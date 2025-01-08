package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.InMemoryGenreService;
import ru.yandex.practicum.filmorate.service.mpa.InMemoryMpaRatingService;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.HashSet;
import java.util.List;

@Repository
@Slf4j
public class JdbcFilmRepository extends BaseRepository<Film> implements FilmStorage {
    private final InMemoryMpaRatingService mpaRatingService;
    private final InMemoryGenreService genreServiceImpl;

    public JdbcFilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper, InMemoryMpaRatingService mpaRatingService,
                              InMemoryGenreService genreServiceImpl) {
        super(jdbc, mapper);
        this.mpaRatingService = mpaRatingService;
        this.genreServiceImpl = genreServiceImpl;
    }

    private static final String DELETE_LIKE_BY_FILM_ID_QUERY = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";
    private static final String DELETE_ALL_FILMS_QUERY = "DELETE FROM films";
    private static final String DELETE_BY_ID_FILM_QUERY = "DELETE FROM films WHERE film_id = ?";
    private static final String FIND_FILM_BY_ID_QUERY = "SELECT * FROM films WHERE film_id=?";
    private static final String FIND_ALL_FILM_QUERY = "SELECT * FROM films";
    private static final String FIND_LIKE_QUERY = "SELECT user_id FROM likes WHERE film_id = ?";
    private static final String ADD_LIKE_TO_FILM_QUERY = """
            INSERT INTO likes(user_id,film_id)
            VALUES (?,?)
            """;
    private static final String UPDATE_FILM_QUERY = """
            UPDATE films
            SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ?
            WHERE film_id = ?
            """;
    private static final String CREATE_FILM_QUERY = """
            INSERT INTO films (name,description,release_date,duration,rating_id)
            VALUES (?,?,?,?,?)
            """;
    private static final String INSERT_GENRES_ID_QUERY = """
            INSERT INTO films_genres (film_id,genre_id)
            VALUES (?,?)
            """;

    private static final String GET_COMMON_FILMS_QUERY = """
            SELECT f.*, COUNT (l.user_id) AS popularity
            FROM films f
            JOIN likes l ON f.film_id = l.film_id
            WHERE l.user_id IN (? , ?)
            GROUP BY f.film_id , f.name
            HAVING COUNT(DISTINCT l.user_id) = 2
            ORDER BY popularity DESC;
            """;

    private static final String GET_POPULAR_FILMS_QUERY = """
            SELECT f.*, COUNT(l.user_id) AS popularity
            FROM films f
            LEFT JOIN likes l ON f.film_id = l.film_id
            LEFT JOIN films_genres fg ON f.film_id = fg.film_id
            WHERE (? IS NULL OR fg.genre_id = ?)
            AND (? IS NULL OR YEAR(f.release_date) = ?)
            GROUP BY f.film_id
            ORDER BY popularity DESC
            """;


    @Override
    public List<Film> getPopularFilms(Integer genreId, Integer year) {
        return findMany(GET_POPULAR_FILMS_QUERY, genreId, genreId, year, year);
    }

    @Override
    public List<Film> getCommonFilms(long userId, long friendId) {
        List<Film> films = findMany(GET_COMMON_FILMS_QUERY, userId, friendId);
        films.forEach(this::setFilmDetails);
        return films;
    }

    @Override
    public void removeLikeFromFilm(long filmId, long userId) {
        getFilmById(filmId);
        delete(DELETE_LIKE_BY_FILM_ID_QUERY, userId, filmId);
    }

    @Override
    public void addLikeToFilm(long filmId, long userId) {
        getFilmById(filmId);
        add(ADD_LIKE_TO_FILM_QUERY, userId, filmId);
    }

    @Override
    public void deleteAllFilms() {
        deleteAll(DELETE_ALL_FILMS_QUERY);
    }

    @Override
    public void deleteFilmById(long filmId) {
        delete(DELETE_BY_ID_FILM_QUERY, filmId);
    }

    @Override
    public Film updateFilm(Film newFilm) {
        update(UPDATE_FILM_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getMpa().getId(),
                newFilm.getId());
        for (Genre genre : newFilm.getGenres()) {
            add(INSERT_GENRES_ID_QUERY, newFilm.getId(), genre.getId());
        }
        setFilmDetails(newFilm);
        return newFilm;
    }

    @Override
    public Film createFilm(Film film) {
        long id = insert(CREATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(id);
        for (Genre genre : film.getGenres()) {
            add(INSERT_GENRES_ID_QUERY, id, genre.getId());
        }
        setFilmDetails(film);
        return film;
    }

    @Override
    public Film getFilmById(long id) {
        log.info("Получение фильма из базы данных ID: {}.", id);
        Film film = findOne(FIND_FILM_BY_ID_QUERY, id).orElseThrow(() -> {
            String errorMessage = "Фильма с ID - " + id + " не существует.";
            return new NotFoundException(errorMessage);
        });
        setFilmDetails(film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> films = findMany(FIND_ALL_FILM_QUERY);
        if (films.isEmpty()) {
            throw new NotFoundException("Фильмы не найдены: база данных пуста.");
        }
        for (Film film : films) {
            setFilmDetails(film);
        }
        return films;
    }

    private void setFilmDetails(Film film) {
        film.setMpa(mpaRatingService.getMpaById(film.getMpa().getId()));
        film.setGenres(genreServiceImpl.getAllGenresForFilm(film.getId()));
        film.setIdUserLike(new HashSet<>(findManyFriendsId(FIND_LIKE_QUERY, film.getId())));
    }
}
