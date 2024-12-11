package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")

public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User postUser(@RequestBody User user) {
        long userId = nextUserId();
        user.setId(userId);
        log.info("Выполнение запроса на создание нового пользователя {}", user);

        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Ошибка валидации логина");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Ошибка валидации электронной почты");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать знак `@`");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Некорректная дата");
            throw new ValidationException("День рождения не может быть в будущем.");
        }

        users.put(userId, user);
        log.info("Добавился новый пользователь");
        return user;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PutMapping
    public User putUser(@RequestBody User putUser) {
        User userTemp = users.get(putUser.getId());
        if (putUser.getEmail() == null) {
            putUser.setEmail(userTemp.getEmail());
        }
        if (putUser.getName() == null) {
            putUser.setName(userTemp.getName());
        }
        if (putUser.getBirthday() == null) {
            putUser.setBirthday(userTemp.getBirthday());
        }
        if (putUser.getLogin() == null) {
            putUser.setLogin(userTemp.getLogin());
        }
        if (putUser.getLogin().contains(" ") || !putUser.getEmail().contains("@")) {
            log.error("Ошибка валидации логина");
            throw new ValidationException("Ошибка валидации логина.");
        }
        if (putUser.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка валидации Дня Рождения");
            throw new ValidationException("Ошибка валидации Дня Рождения.");
        }
        if (!users.containsKey(putUser.getId())) {
            throw new ValidationException("Пользователя с таким айди нет.");
        }
        users.put(putUser.getId(), putUser);
        log.info("Обновился пользователь с айди = " + putUser.getId());
        return putUser;
    }


    private long nextUserId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}