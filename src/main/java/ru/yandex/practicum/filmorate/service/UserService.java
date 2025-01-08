package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final Validator validator;
    private final UserStorage userDbStorage;
    private long userId = 0;

    @Autowired
    public UserService(Validator validator, UserStorage userDbStorage) {
        this.validator = validator;
        this.userDbStorage = userDbStorage;
    }

    public User create(User user) {
        validationUser(user);
        userId++;
        user.setId(userId);
        return userDbStorage.create(user);
    }

    public void validationUser(User user) {
        Validator.validationUser(user);
    }

    public User update(User user) {
        validationUser(user);
        if (userDbStorage.getUserById(user.getId()).isEmpty()) {
            throw new DataNotFoundException("Нет такого id");
        }
        return userDbStorage.update(user);
    }

    public void delete(long id) {
        userDbStorage.delete(id);
    }


    public Collection<User> findAll() {
        return userDbStorage.getAllUsers();
    }

    public void addFriend(long id, long friendId) {
        if (userDbStorage.getUserById(id).isEmpty() || userDbStorage.getUserById(friendId).isEmpty()) {
            throw new DataNotFoundException("Нет такого id");
        }
        userDbStorage.addFriend(id, friendId);
    }

    public Optional<User> getUser(Long id) {
        if (userDbStorage.getUserById(id).isEmpty()) {
            throw new DataNotFoundException("Нет такого id");
        }
        return userDbStorage.getUserById(id);
    }

    public List<User> getFriendsList(long id) {
        if (userDbStorage.getUserById(id).isEmpty()) {
            throw new DataNotFoundException("Нет такого id");
        }
        return userDbStorage.getFriendsList(id);
    }

    public void deleteFriend(long id, long friendId) {
        if (userDbStorage.getUserById(id).isEmpty() || userDbStorage.getUserById(friendId).isEmpty()) {
            throw new DataNotFoundException("Нет такого id");
        }
        userDbStorage.deleteFriend(id, friendId);
    }

    public List<User> getCommonFriend(Long id, Long otherId) {
        if (userDbStorage.getUserById(id).isEmpty() || userDbStorage.getUserById(otherId).isEmpty()) {
            throw new DataNotFoundException("Нет такого id");
        }
        List<User> firstFriendsList = userDbStorage.getFriendsList(id);
        List<User> secondFriendsList = userDbStorage.getFriendsList(otherId);
        if (firstFriendsList.isEmpty() || secondFriendsList.isEmpty()) {
            return new ArrayList<>();
        }
        return firstFriendsList.stream().filter(secondFriendsList::contains)
                .collect(Collectors.toList());
    }
}