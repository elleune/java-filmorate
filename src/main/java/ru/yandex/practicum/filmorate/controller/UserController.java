package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос на создание пользователя: {}", user.toString());
        return userService.create(user);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        return userService.update(newUser);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable("id") Long id) {
        userService.remove(id);
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") Long userId) {
        List<User> userFriends = userService.getFriends(userId);
        log.info("Метод GET /users/{id}/friends вернул ответ {}", userFriends);
        return userFriends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getFriendsCommonOther(@PathVariable("id") Long userId,
                                            @PathVariable("otherId") Long otherId) {
        List<User> userFriends = userService.getFriendsCommonOther(userId, otherId);
        log.info("Метод GET /users/{id}/friends/common/{otherId} вернул ответ {}", userFriends);
        return userFriends;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Long userId,
                          @PathVariable("friendId") Long friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") Long userId,
                             @PathVariable("friendId") Long friendId) {
        userService.removeFriend(userId, friendId);
    }
}
