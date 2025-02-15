package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserService {
    @Autowired
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Integer id, Integer friendId) throws ValidationException {
        User user = userStorage.findUserById(id);
        User friend = userStorage.findUserById(friendId);
        if (user != null && friend != null) {
            user.addFriend(friendId);
            userStorage.update(user);
        }
    }

    public void deleteFriend(Integer id, Integer friendId) throws ValidationException {
        User user = userStorage.findUserById(id);
        User friend = userStorage.findUserById(friendId);
        user.deleteFriend(friendId);
        userStorage.update(user);
    }

    public List<User> getAllFriends(Integer userId) {
        User user = userStorage.findUserById(userId);
        List<User> users = new ArrayList<>();
        if (user.getFriends() != null) {
            user.getFriends().forEach(id -> users.add(userStorage.findUserById(id)));
        }
        return users;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(Integer id) {
        return userStorage.findUserById(id);
    }

    public User create(User user) throws ValidationException {
        return userStorage.create(user);
    }

    public User update(User user) throws ValidationException, ResourceNotFoundException {
        return userStorage.update(user);
    }

    public List<User> findAllCommonFriends(Integer userId, Integer otherId) {
        List<User> commonFriends = new ArrayList<>();
        User user = userStorage.findUserById(userId);
        List<User> users = new ArrayList<>();
        if (user.getFriends() != null) {
            user.getFriends().forEach(id -> users.add(userStorage.findUserById(id)));
        }
        User user2 = userStorage.findUserById(otherId);
        List<User> users2 = new ArrayList<>();
        if (user2.getFriends() != null) {
            user2.getFriends().forEach(id -> users2.add(userStorage.findUserById(id)));
        }
        for (User usr : users) {
            if (users2.contains(usr)) {
                commonFriends.add(usr);
            }
        }
        return commonFriends;
    }

    public boolean checkUserExist(Integer id) throws Exception {
        return userStorage.checkUserExist(id);
    }
}