package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dal.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Component
@Primary
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    final GenreRepository genreRepository;

    @Override
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    @Override
    public Optional<Genre> getById(long id) {
        return genreRepository.findById(id);
    }
}
