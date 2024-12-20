package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    void deleteAllUsers();

    User getUserById(long id);

    List<User> findAll();

    User update(User newUser);

    User create(User user);
}