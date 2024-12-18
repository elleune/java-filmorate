package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Validator.Validator;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final Validator validator;
    private final UserStorage userStorage;
    private long userId = 0;

    @Autowired
    public UserService(Validator validator, InMemoryUserStorage userStorage) {
        this.validator = validator;
        this.userStorage = userStorage;
    }

    public void validateUser(User user) {
        validator.validationUser(user);
    }

    public List<User> allUser() {
        log.debug("Текущее количество пользователей: {}", userStorage.getUsers().size());
        return new ArrayList<>(userStorage.getUsers().values());
    }

    public User createUser(User user) {
        validateUser(user);
        userId++;
        user.setId(userId);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        Map<Long, User> actualUsers = userStorage.getUsers();
        if (!actualUsers.containsKey(user.getId())) {
            throw new NotFoundException("Нет такого id");
        }
        validateUser(user);
        return userStorage.updateUser(user);
    }

    public void deleteUser(long id) {
        User user = userStorage.getUserById(id);
        validateUser(user);
        userStorage.deleteUser(user);
    }

    public void addFriend(long id, long friendId) {
        User firstFriend = userStorage.getUserById(id);
        User secondFriend = userStorage.getUserById(friendId);
        firstFriend.getFriends().add(friendId);
        secondFriend.getFriends().add(id);
        log.debug("Пользователи {} и {} теперь друзья", firstFriend.getName(),
                secondFriend.getName());
    }

    public void deleteFriend(long id, long friendId) {
        User firstFriend = userStorage.getUserById(id);
        User secondFriend = userStorage.getUserById(friendId);
        if (firstFriend.getFriends().contains(friendId)) {
            firstFriend.getFriends().remove(friendId);
        }
        if (secondFriend.getFriends().contains(friendId)) {
            secondFriend.getFriends().remove(id);
            log.debug("Пользователи {} и {} теперь не друзья", firstFriend.getName(),
                    secondFriend.getName());
        }
    }

    public List<User> getCommonFriend(long id, long otherId) {
        Map<Long, User> actualUsers = userStorage.getUsers();
        User firstFriend = userStorage.getUserById(id);
        User secondFriend = userStorage.getUserById(otherId);
        List<Long> firstFriendsList = firstFriend.getFriends();
        List<Long> secondFriendsList = secondFriend.getFriends();
        log.debug("Получен список общих друзей пользователей {} и {}", firstFriend.getName(),
                secondFriend.getName());
        if (firstFriendsList.isEmpty() || secondFriendsList.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> commonIdList = firstFriendsList.stream().filter(secondFriendsList::contains)
                .collect(Collectors.toList());
        return commonIdList.stream().map(actualUsers::get).collect(Collectors.toList());
    }

    public User getUser(Long id) {
        return userStorage.getUserById(id);
    }

    public List<User> getFriendsList(long id) {
        User user = userStorage.getUserById(id);
        Map<Long, User> actualUsers = userStorage.getUsers();
        log.debug("Получен список друзей пользователя {}", user.getName());
        return user.getFriends().stream().map(actualUsers::get).collect(Collectors.toList());
    }
}