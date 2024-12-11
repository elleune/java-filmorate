package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    static final LocalDate DATE_BIRTHDAY_CINEMA = LocalDate.of(1895, 12, 28);

    @PostMapping
    public Film postFilm(@RequestBody Film film) {
        long filmId = nextFilmId();
        film.setId(filmId);
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Ошибка валидации фильма");
            throw new ValidationException("Название не может быть пустым.");
        }
        if (film.getDescription() == null || film.getDescription().isBlank() || film.getDescription().length() > 200) {
            log.error("Ошибка валидации описания");
            throw new ValidationException("Максимальная длина описания - 200 символов.");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(DATE_BIRTHDAY_CINEMA)) {
            log.error("Ошибка валидации даты создания");
            throw new ValidationException("Дата реализации - не раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() <= 0) {
            log.error("Ошибка валидации продолжительности фильма");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }
        films.put(filmId, film);
        log.info("Новый фильм создан");
        return film;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PutMapping
    public Film putFilm(@RequestBody Film filmPut) {
        Film filmTemp = films.get(filmPut.getId());
        if (filmPut.getReleaseDate() == null) {
            filmPut.setReleaseDate(filmTemp.getReleaseDate());
        }
        if (filmPut.getName() == null) {
            filmPut.setName(filmTemp.getName());
        }
        if (filmPut.getDescription() == null) {
            filmPut.setDescription(filmTemp.getDescription());
        }
        if (filmPut.getDuration() == null) {
            filmPut.setDuration(filmTemp.getDuration());
        }
        if (filmPut.getDescription().length() > 200) {
            log.error("Описание > 200");
            throw new ValidationException("Ошибка валидации описания.");
        }
        if (filmPut.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза позже 28.12.1895");
            throw new ValidationException("Ошибка валидации даты создания.");
        }
        if (filmPut.getDuration() <= 0) {
            log.error("Отрицательная длина фильма");
            throw new ValidationException("Ошибка валидации продолжительности фильма.");
        }
        if (!films.containsKey(filmPut.getId())) {
            throw new ValidationException("Фильма с таким ID нет.");
        }
        films.put(filmPut.getId(), filmPut);
        log.info("Фильм изменен");
        return filmPut;
    }


    private long nextFilmId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}