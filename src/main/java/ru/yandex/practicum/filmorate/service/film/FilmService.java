package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    List<Film> getPopularFilms(long limit, Integer genreId, Integer year);


    List<Film> getCommonFilms(long userId, long friendId);


    Film createFilm(Film film);


    Film updateFilm(Film newFilm);


    Film getFilmById(long id);

    List<Film> getAllFilms();

    void deleteFilmById(long filmId);


    void deleteAllFilms();


    void addLikeToFilm(long filmId, long userId);

    void removeLikeFromFilm(long filmId, long userId);
}
