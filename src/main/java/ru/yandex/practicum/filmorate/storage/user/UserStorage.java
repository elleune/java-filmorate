package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User create(User user);

    Optional<User> getById(Long userId);

    User update(User user);

    void remove(Long userId);

    List<User> getAll();

    List<User> getFriends(User user);

    List<User> getFriendsCommonOther(User user, User otherUser);

}
