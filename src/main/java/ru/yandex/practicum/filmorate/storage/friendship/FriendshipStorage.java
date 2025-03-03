package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

public interface FriendshipStorage {

    List<Friendship> getAll();

    void create(Long userId, Long friendId);

    void remove(Long userId, Long friendId);

}
