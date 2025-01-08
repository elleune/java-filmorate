package ru.yandex.practicum.filmorate.dao.genry;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    Optional<Genre> getGenreById(long id);

    List<Genre> getAllGenre();
}