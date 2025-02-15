package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    public Film create(Film film) throws ValidationException;

    public Film update(Film film) throws ValidationException;

    public Collection<Film> findAll();

    public Film findById(Integer id);

    boolean checkFilmExist(Integer id);
}