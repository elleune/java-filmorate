package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public Optional<User> get(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping("/users")
    public User put(@RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping("/users/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        userService.addFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriendsList(@PathVariable("id") Long id) {
        return userService.getFriendsList(id);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriendsList(@PathVariable("id") Long id, @PathVariable("otherId") Long otherId) {
        return userService.getCommonFriend(id, otherId);
    }
}