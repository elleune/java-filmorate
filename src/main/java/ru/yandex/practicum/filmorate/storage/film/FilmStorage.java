package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> getAllFilms();

    Film create(Film film);

    Optional<Film> update(Film film);

    void delete(long id);

    Optional<Film> getFilmById(long id);

    void addLike(long id, long userId);

    void removeLike(int id, int userId);

    List<Film> getPopularFilms(int count);
}