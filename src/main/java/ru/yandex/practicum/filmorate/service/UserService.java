package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;
    private long userId = 0L;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void validationUser(User user) {
        Validator.validationUser(user);
    }

    public List<User> findAll() {
        return new ArrayList<>(userStorage.getUsers().values());
    }

    public User create(User user) {
        validationUser(user);
        userId++;
        user.setId(userId);
        return userStorage.create(user);
    }

    public User update(User user) {
        Map<Long, User> actualUsers = userStorage.getUsers();
        if (!actualUsers.containsKey(user.getId())) {
            throw new DataNotFoundException("Нет такого id");
        }
        validationUser(user);
        return userStorage.update(user);
    }

    public void delete(long id) {
        User user = userStorage.getUserById(id);
        validationUser(user);
        userStorage.delete(user);
    }

    public void addFriend(long id, long friendId) {
        User firstFriend = userStorage.getUserById(id);
        User secondFriend = userStorage.getUserById(friendId);
        firstFriend.getFriendsId().add(friendId);
        secondFriend.getFriendsId().add(id);
        secondFriend.getName();
    }

    public void deleteFriend(long id, long friendId) {
        User user = userStorage.getUserById(id);
        User userFriend = userStorage.getUserById(friendId);
        user.getFriendsId().remove(friendId);
        userFriend.getFriendsId().remove(id);
    }

    public List<User> getCommonFriend(long id, long otherId) {
        Map<Long, User> actualUsers = userStorage.getUsers();
        User firstFriend = userStorage.getUserById(id);
        User secondFriend = userStorage.getUserById(otherId);
        List<Long> firstFriendsList = firstFriend.getFriendsId();
        List<Long> secondFriendsList = secondFriend.getFriendsId();
        if (firstFriendsList.isEmpty() || secondFriendsList.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> commonIdList = firstFriendsList.stream().filter(secondFriendsList::contains)
                .toList();
        return commonIdList.stream().map(actualUsers::get).collect(Collectors.toList());
    }

    public List<User> getFriendsList(long id) {
        User user = userStorage.getUserById(id);
        Map<Long, User> actualUsers = userStorage.getUsers();
        return user.getFriendsId().stream().map(actualUsers::get).collect(Collectors.toList());
    }
}