package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {


    User createUser(User user);

    User updateUser(User user);

    void addFriend(long userId, long friendId, String status);

    User getUserById(long userId);

    void updateStatus(long userId, long friendId, String status);

    void removeFriend(long userId, long friendId);

    List<User> getAllFriends(long userId);

    void deleteAllUsers();

    void deleteUserById(long userId);

    List<User> getAllUsers();

}
