package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryMpaStorage implements MpaStorage {
    Map<Long, Mpa> ratings = new HashMap<>();

    @Override
    public List<Mpa> getAll() {
        return ratings.values().stream().toList();
    }

    @Override
    public Optional<Mpa> getById(long id) {
        return ratings.get(id) == null ? Optional.empty() : Optional.of(ratings.get(id));
    }


}
