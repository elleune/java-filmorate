package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    @Autowired
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(Integer id, Integer userId) throws ValidationException {
        Film film = filmStorage.findById(id);
        film.setLike(userId);
        filmStorage.update(film);
    }

    public void deleteLike(Integer id, Integer userId) throws ValidationException {
        Film film = filmStorage.findById(id);
        if (userId < 0) {
            throw new ResourceNotFoundException("Пользователь не найден");
        }
        if (film != null) {
            film.deleteLike(userId);
            filmStorage.update(film);
        }
    }

    public List<Film> getMostLikedFilms(Integer count) {
        return filmStorage.findAll()
                .stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Film> findAll() {
        return new ArrayList<>(filmStorage.findAll());
    }

    public Film findById(Integer id) {
        return filmStorage.findById(id);
    }

    public Film create(Film film) throws ValidationException {
        return filmStorage.create(film);
    }

    public Film update(Film film) throws ValidationException {
        return filmStorage.update(film);
    }

    private int compare(Film film1, Film film2) {
        if (film1.getLikes() == null) {
            film1.setLikes(new HashSet<>());
        }
        if (film2.getLikes() == null) {
            film2.setLikes(new HashSet<>());
        }
        return Integer.compare(film2.getLikes().size(), film1.getLikes().size());
    }

    public boolean checkFilmExist(Integer id) {
        return filmStorage.checkFilmExist(id);
    }
}