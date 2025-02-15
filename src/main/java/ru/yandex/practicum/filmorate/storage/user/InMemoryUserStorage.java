package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.Collection;
import java.util.HashMap;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 0;

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(@RequestBody User user) throws ValidationException {
        Validator.validationUser(user);
        id++;
        user.setId(id);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(@RequestBody User user) throws ValidationException {
        Validator.validationUser(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (users.containsKey(user.getId())) {
            User tmpUser = users.get(user.getId());
            tmpUser.setEmail(user.getEmail());
            tmpUser.setLogin(user.getLogin());
            tmpUser.setBirthday(user.getBirthday());
            tmpUser.setName(user.getName());
            users.replace(tmpUser.getId(), tmpUser);
            user = tmpUser;
        } else {
            throw new ResourceNotFoundException("Пользователя с таким id не существует");
        }
        return user;
    }

    @Override
    public User findUserById(Integer id) {
        User user = users.get(id);
        if (user == null) {
            throw new ResourceNotFoundException("Пользователя с таким id не существует");
        } else {
            return user;
        }
    }

    @Override
    public boolean checkUserExist(Integer id) {
        return true;
    }
}