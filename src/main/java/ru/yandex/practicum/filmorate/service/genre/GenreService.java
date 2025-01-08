package ru.yandex.practicum.filmorate.service.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreService {
    Genre getGenreById(int id);

    List<Genre> getAllGenres();

    Set<Genre> getAllGenresForFilm(long filmId);
}
