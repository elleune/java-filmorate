package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(long userId, long friendId) {
        User user = userStorage.getUserById(userId);
        User userFriend = userStorage.getUserById(friendId);
        user.getFriendsId().add(friendId);
        userFriend.getFriendsId().add(userId);
        log.info("Пользователь {} добавил в друзья {}.", user.getName(), userFriend.getName());
    }


    public void removeFriend(long userId, long friendId) {
        User user = userStorage.getUserById(userId);
        User userFriend = userStorage.getUserById(friendId);
        user.getFriendsId().remove(friendId);
        userFriend.getFriendsId().remove(userId);
        log.info("Пользователь {} удалил из друзей {}.", user.getName(), userFriend.getName());
    }


    public List<User> getFriends(long userId) {
        User user = userStorage.getUserById(userId);
        log.info("Получение списка всех друзей пользователя {}.", user.getName());
        return new ArrayList<>(user.getFriendsId().stream()
                .map(userStorage::getUserById)
                .toList());

    }


    public List<User> getCommonFriends(long userId, long otherId) {
        User user = userStorage.getUserById(userId);
        User userOther = userStorage.getUserById(otherId);
        List<User> commonFriends = new ArrayList<>();
        for (Long id : user.getFriendsId()) {
            if (userOther.getFriendsId().contains(id)) {
                commonFriends.add(userStorage.getUserById(id));
            }
        }
        log.info("Получение списка общих друзей пользователя {} с пользователем {}.", user.getName(), userOther.getName());
        return commonFriends;
    }

    public User getUserById(long id) {
        return userStorage.getUserById(id);
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User newUser) {
        return userStorage.update(newUser);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }
}