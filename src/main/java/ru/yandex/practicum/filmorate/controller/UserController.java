package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.Validator.Validator;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")

public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private final Validator validator = new Validator();
    private int userId;

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Выполнение запроса на создание нового пользователя {}", user);
        userId++;
        user.setId(userId);
        validator.validationUser(user);
        users.put(user.getId(), user);
        log.debug("Создан новый пользователь с именем: {}", user.getName());
        return user;
    }

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        validator.validationUser(user);
        if (!users.containsKey(user.getId())) {
            throw new RuntimeException("Нет такого id");
        }
        users.put(user.getId(), user);
        log.debug("Обновлены данные пользователя с именем: {}", user.getName());
        return user;
    }
}