package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User create(User user);

    User update(User user);

    void delete(Long id);

    Optional<User> getUserById(long id);

    public Collection<User> getAllUsers();

    public void addFriend(long id, long friendId);

    public List<User> getFriendsList(long id);

    void deleteFriend(long id, long friendId);
}