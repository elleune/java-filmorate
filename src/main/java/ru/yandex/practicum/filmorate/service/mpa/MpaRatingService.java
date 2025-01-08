package ru.yandex.practicum.filmorate.service.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaRatingService {
    List<Mpa> getAll();

    Mpa getMpaById(int id);
}
