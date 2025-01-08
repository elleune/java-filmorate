package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> getPopularFilms(Integer genreId, Integer year);

    List<Film> getCommonFilms(long userId, long friendId);

    void removeLikeFromFilm(long filmId, long userId);

    void addLikeToFilm(long filmId, long userId);

    void deleteAllFilms();

    void deleteFilmById(long filmId);

    Film updateFilm(Film newFilm);

    Film createFilm(Film film);

    Film getFilmById(long id);

    List<Film> getAllFilms();
}
