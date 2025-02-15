package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private Validator validator = new Validator();

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @RequestMapping("/{id}")
    @GetMapping
    public User findById(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @RequestMapping("/{id}/friends")
    @GetMapping
    public List<User> findAllFriends(@PathVariable Integer id) {
        return userService.getAllFriends(id);
    }

    @RequestMapping("/{id}/friends/common/{otherId}")
    @GetMapping
    public List<User> findAllCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.findAllCommonFriends(id, otherId);
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException, ResourceNotFoundException {
        validator.validationUser(user);
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) throws Exception {
        if (!userService.checkUserExist(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        }
        validator.validationUser(user);
        return userService.update(user);
    }

    @RequestMapping(value = "/{id}/friends/{friendId}", method = RequestMethod.PUT)
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) throws ValidationException {
        userService.addFriend(id, friendId);
    }

    @RequestMapping(value = "/{id}/friends/{friendId}", method = RequestMethod.DELETE)
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) throws ValidationException {
        userService.deleteFriend(id, friendId);
    }
}