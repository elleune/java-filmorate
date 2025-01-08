package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    User createUser(User user);


    User updateUser(User user);

    void addFriend(long userId, long friendId);

    User getUserById(long userId);


    List<User> getAllUsers();

    List<User> getUserFriends(long userId);

    List<User> getCommonFriends(long userId, long otherId);

    void removeFriend(long userId, long friendId);

    void deleteUserById(long userId);

    void deleteAllUsers();
}
