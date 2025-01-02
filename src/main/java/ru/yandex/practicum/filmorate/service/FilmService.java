package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final Validator validator;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private Long filmId = 0L;

    @Autowired
    public FilmService(Validator validator, FilmStorage filmStorage, UserStorage userStorage) {
        this.validator = validator;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void validationFilm(Film film) {
        validator.validationFilm(film);
    }

    public List<Film> findAll() {
        return new ArrayList<>(filmStorage.getFilms().values());
    }

    public Film create(Film film) {
        validationFilm(film);
        filmId++;
        film.setId(filmId);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        Map<Long, Film> actualUsers = filmStorage.getFilms();
        if (!actualUsers.containsKey(film.getId())) {
            throw new DataNotFoundException("Нет такого фильма");
        }
        validationFilm(film);
        return filmStorage.update(film);
    }

    public void delete(long id) {
        Film film = filmStorage.getFilmById(id);
        validationFilm(film);
        filmStorage.delete(film);
    }

    public void addLike(long id, long userId) {
        Film film = filmStorage.getFilmById(id);
        userStorage.getUserById(userId);
        film.addLike(userId);
    }

    public void deleteLike(long id, long userId) {
        Film film = filmStorage.getFilmById(id);
        userStorage.getUserById(userId);
        film.removeLike(userId);
    }


    public List<Film> getPopularFilms(int count) {
        return filmStorage.getFilms().values().stream()
                .sorted(Comparator.comparing(Film::getRate).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }
}