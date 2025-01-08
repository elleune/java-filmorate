package ru.yandex.practicum.filmorate.dao.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaDao {

    Optional<Mpa> getMpaById(long id);

    List<Mpa> getAllMpa();

}