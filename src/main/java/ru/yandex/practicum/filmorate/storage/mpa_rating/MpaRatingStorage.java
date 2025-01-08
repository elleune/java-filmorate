package ru.yandex.practicum.filmorate.storage.mpa_rating;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaRatingStorage {
    List<Mpa> getAllMpas();

    Mpa getMpaById(int id);
}