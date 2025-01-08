package ru.yandex.practicum.filmorate.storage.user;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();


    @Override
    public User createUser(User user) {
        Validator.validateUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User newUser) {
        if (newUser.getId() == null) {
            throw new ValidationException("ID должен быть указан.");
        }
        if (users.get(newUser.getId()) == null) {
            throw new NotFoundException("Пользователь с ID - " + newUser.getId() + " не найден");
        }
        Validator.validateUser(newUser);
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public void addFriend(long userId, long friendId, String status) {

    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователя с ID = " + userId + " не существует.");
        }
        return users.get(userId);
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
    }

    @Override
    public void updateStatus(long userId, long friendId, String status) {
    }

    @Override
    public void removeFriend(long userId, long friendId) {
    }

    @Override
    public List<User> getAllFriends(long userId) {
        return List.of();
    }

    @Override
    public void deleteUserById(long userId) {
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
