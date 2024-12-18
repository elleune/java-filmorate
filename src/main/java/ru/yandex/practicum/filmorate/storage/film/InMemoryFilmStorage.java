package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    Map<Long, Film> films = new HashMap<>();

    @Override
    public Map<Long, Film> getFilms() {
        return films;
    }

    @Override
    public Film createFilm(Film film) {
        films.put(film.getId(), film);
        log.debug("Создан новый фильм с названием: {}", film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new RuntimeException("Нет такого id");
        }
        films.put(film.getId(), film);
        log.debug("Обновлены данные пользователя с именем: {}", film.getName());
        return film;
    }

    @Override
    public void deleteFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new RuntimeException("Нет такого id");
        }
        films.remove(film.getId());
        log.debug("Пользователь с именем: {} удален", film.getName());
    }

    @Override
    public Film getFilmById(long id) {
        Map<Long, Film> actualFilms = getFilms();
        if (!actualFilms.containsKey(id)) {
            throw new NotFoundException("Нет такого id - фильма");
        }
        return actualFilms.get(id);
    }
}