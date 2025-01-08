package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilmService {
    private final Validator validator;
    private final FilmStorage filmDbStorage;
    private final UserStorage userDbStorage;
    private Long filmId = 0L;

    @Autowired
    public FilmService(Validator validator, FilmStorage filmDbStorage, UserStorage userDbStorage) {
        this.validator = validator;
        this.filmDbStorage = filmDbStorage;
        this.userDbStorage = userDbStorage;
    }

    public void validationFilm(Film film) {
        validator.validationFilm(film);
    }

    public Film create(Film film) {
        validationFilm(film);
        filmId++;
        film.setId(filmId);
        return filmDbStorage.create(film);
    }

    public Optional<Film> update(Film filmUp) {
        if (filmDbStorage.getFilmById(filmUp.getId()).isEmpty()) {
            throw new DataNotFoundException("Нет такого id");
        }
        return filmDbStorage.update(filmUp);
    }

    public Optional<Film> getFilm(long id) {
        if (filmDbStorage.getFilmById(id).isEmpty()) {
            throw new DataNotFoundException("Нет такого id");
        }
        return filmDbStorage.getFilmById(id);
    }

    public void addLike(long id, long userId) {
        filmDbStorage.addLike(id, userId);
    }

    public void deleteLike(int id, int userId) {
        if (userDbStorage.getUserById(userId).isEmpty()) {
            throw new DataNotFoundException("Нет такого id - пользователя");
        }
        filmDbStorage.removeLike(id, userId);
    }

    public List<Film> findAll() {
        return filmDbStorage.getAllFilms();
    }

    public List<Film> getPopularFilms(int count) {
        return filmDbStorage.getPopularFilms(count);
    }

    public void delete(long id) {
        if (filmDbStorage.getFilmById(id).isEmpty()) {
            throw new DataNotFoundException("Нет такого id - фильма");
        }
        filmDbStorage.delete(id);
    }
}