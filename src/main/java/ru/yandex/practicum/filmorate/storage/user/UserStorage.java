package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {

    Map<Long, User> getUsers();

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(User user);

    User getUserById(long id);
}