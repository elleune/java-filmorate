package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> getAll();

    Optional<User> getById(long id);

    User create(User user);

    User update(User user);

    List<User> findFriendsById(long id);

    List<User> findCommonFriends(long id, long otherId);

    void clear();
}
