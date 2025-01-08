package ru.yandex.practicum.filmorate.storage.film;


import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    public Film createFilm(Film film) {
        Validator.validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film newFilm) {
        if (newFilm.getId() == null) {
            throw new ValidationException("ID должен быть указан.");
        }
        if (!films.containsKey(newFilm.getId())) {
            throw new NotFoundException("Фильм с ID = " + newFilm.getId() + " не найден");
        }
        Validator.validateFilm(newFilm);
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    public Film getFilmById(long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильма с ID = " + id + " не существует.");
        }
        return films.get(id);
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }


    @Override
    public List<Film> getPopularFilms(Integer genreId, Integer year) {
        return List.of();
    }

    @Override
    public List<Film> getCommonFilms(long userId, long friendId) {
        return List.of();
    }

    @Override
    public void removeLikeFromFilm(long filmId, long userId) {

    }


    @Override
    public void addLikeToFilm(long filmId, long userId) {

    }


    @Override
    public void deleteAllFilms() {
        films.clear();
    }


    @Override
    public void deleteFilmById(long id) {
        films.remove(id);
    }


    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
