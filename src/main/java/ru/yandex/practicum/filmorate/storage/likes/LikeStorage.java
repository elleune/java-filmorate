package ru.yandex.practicum.filmorate.storage.likes;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;

public interface LikeStorage {

    List<Like> findAll();

    void create(long userId, long filmId);

    void remove(long userId, long filmId);

    void clear();
}
