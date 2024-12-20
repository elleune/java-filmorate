package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    void deleteAllFilms();

    Film getFilmById(long id);

    Film updateFilm(Film newFilm);

    Film createFilm(Film film);

    List<Film> findAllFilms();
}