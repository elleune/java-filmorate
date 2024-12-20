package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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

    public User create(User user) {
        Validator.validationUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Создан новый пользователь - {} , ID - {}.", user.getLogin(), user.getId());
        return user;
    }

    public User update(User newUser) {
        if (newUser.getId() == null) {
            log.warn("При обновлении данных не указали ID.");
            throw new ValidationException("ID должен быть указан.");
        }
        if (users.get(newUser.getId()) == null) {
            log.warn("В базе нет пользователя с ID - {}.", newUser.getId());
            throw new NotFoundException("Пользователь с ID - " + newUser.getId() + " не найден");
        }
        Validator.validationUser(newUser);
        users.put(newUser.getId(), newUser);
        log.info("Информация о пользователе - {} , ID - {} , обновлена.", newUser.getLogin(), newUser.getId());
        return newUser;
    }


    public List<User> findAll() {
        log.info("Получение всех пользователей.");
        return new ArrayList<>(users.values());
    }

    public User getUserById(long id) {
        if (!users.containsKey(id)) {
            log.warn("При пользователя указали неверный ID.");
            throw new NotFoundException("Пользователя с ID = " + id + " не существует.");
        }
        log.info("Получение пользователя по ID.");
        return users.get(id);
    }

    public void deleteAllUsers() {
        users.clear();
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