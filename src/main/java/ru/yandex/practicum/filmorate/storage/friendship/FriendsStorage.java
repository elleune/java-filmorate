package ru.yandex.practicum.filmorate.storage.friendship;


import ru.yandex.practicum.filmorate.model.Friends;

import java.util.List;

public interface FriendsStorage {
    List<Friends> findAll();

    void create(long userId, long friendId);

    void remove(long userId, long friendId);

    void clear();
}
