package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.Collection;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final Validator validator;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, Validator validator) {
        this.userStorage = userStorage;
        this.validator = validator;

    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        validator.validationUser(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        validator.validationUser(user);
        validateUserExists(user.getId());
        return userStorage.update(user);
    }

    public User getUserById(Long id) {
        return userStorage.findById(id);
    }

    public boolean addFriend(Long userId, Long friendId) {
        validateUserExists(userId);
        validateUserExists(friendId);
        return userStorage.addFriend(userId, friendId);
    }

    public boolean removeFriend(Long userId, Long friendId) {
        validateUserExists(userId);
        validateUserExists(friendId);
        return userStorage.removeFriend(userId, friendId);
    }

    public Collection<User> getFriends(Long id) {
        validateUserExists(id);
        return userStorage.getFriends(id);
    }

    public Collection<User> getCommonFriends(Long id, Long otherId) {
        validateUserExists(id);
        validateUserExists(otherId);
        return userStorage.getCommonFriends(id, otherId);
    }

    public void validateUserExists(Long id) {
        if (userStorage.findById(id) == null) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }
}