package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryGenreStorage implements GenreStorage {
    Map<Long, Genre> genres = new HashMap<>();

    @Override
    public List<Genre> getAll() {
        return genres.values().stream().toList();
    }

    @Override
    public Optional<Genre> getById(long id) {
        return genres.get(id) == null ? Optional.empty() : Optional.of(genres.get(id));
    }
}
