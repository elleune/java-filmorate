package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    static final String NOT_FOUND_MESSAGE = "Жанр с id = %s не найден";
    final GenreStorage genreStorage;

    public List<Genre> findAll() {
        return genreStorage.getAll();
    }

    public Genre findById(Long id) {
        Optional<Genre> genre = genreStorage.getById(id);
        if (genre.isPresent()) {
            return genre.get();
        }
        throw new NotFoundException(String.format(NOT_FOUND_MESSAGE, id));
    }
}