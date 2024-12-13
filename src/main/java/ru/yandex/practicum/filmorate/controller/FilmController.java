package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.Validator.Validator;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private final Validator validator = new Validator();
    private int filmId = 0;

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        filmId++;
        film.setId(filmId);
        validator.validationFilm(film);
        films.put(film.getId(), film);
        log.debug("Добавлен новый фильм с названием: {}", film.getName());
        return film;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        validator.validationFilm(film);
        if (!films.containsKey(film.getId())) {
            throw new RuntimeException("Нет такого id");
        }
        films.put(film.getId(), film);
        log.debug("Обновлены данные фильма с названием: {}", film.getName());
        return film;
    }
}