package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    static final String NOT_FOUND_MESSAGE = "Пользователь с id = %s не найден";
    final UserStorage userStorage;
    final FriendshipStorage friendshipStorage;

    public List<User> findAll() {
        return userStorage.getAll();
    }

    public User findById(Long id) {
        Optional<User> user = userStorage.getById(id);
        if (user.isPresent()) {
            return user.get();
        }
        log.error(String.format(NOT_FOUND_MESSAGE, id));
        throw new NotFoundException(String.format(NOT_FOUND_MESSAGE, id));
    }

    public User create(User user) {
        validate(user);
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Не указано имя пользователя. Приравниваем его к логину");
            user.setName(user.getLogin());
        }

        User createdUser = userStorage.create(user);
        log.info("Пользователь создан");
        log.debug(createdUser.toString());
        return createdUser;
    }

    public User update(User user) {
        if (user.getId() == null) {
            log.error("Не указан id пользователя");
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        Optional<User> userOptional = userStorage.getById(user.getId());

        if (userOptional.isPresent()) {
            validate(user);
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            } else {
                log.warn("Не указано имя пользователя. Приравниваем его к логину");
                user.setName(user.getName());
            }
            User currentUser = userStorage.update(user);
            log.info("Пользователь обновлен");
            log.debug(currentUser.toString());
            return currentUser;
        } else {
            log.error(String.format(NOT_FOUND_MESSAGE, user.getId()));
            throw new NotFoundException(String.format(NOT_FOUND_MESSAGE, user.getId()));
        }
    }

    public List<User> findFriends(Long id) {
        Optional<User> user = userStorage.getById(id);
        if (user.isPresent()) {
            return userStorage.findFriendsById(id);
        } else {
            log.error(String.format(NOT_FOUND_MESSAGE, id));
            throw new NotFoundException(String.format(NOT_FOUND_MESSAGE, id));
        }
    }

    public void addFriend(Long id, Long friendId) {
        Optional<User> user = userStorage.getById(id);
        if (user.isPresent()) {
            Optional<User> friend = userStorage.getById(friendId);
            if (friend.isPresent()) {
                friendshipStorage.create(id, friendId);
                log.info("Пользователь с id = {} добавил друга с id = {}", id, friendId);
            } else {
                log.error(String.format(NOT_FOUND_MESSAGE, friendId));
                throw new NotFoundException(String.format(NOT_FOUND_MESSAGE, friendId));
            }
        } else {
            log.error(String.format(NOT_FOUND_MESSAGE, id));
            throw new NotFoundException(String.format(NOT_FOUND_MESSAGE, id));
        }
    }

    public void removeFriend(Long id, Long friendId) {
        Optional<User> user = userStorage.getById(id);
        if (user.isPresent()) {
            Optional<User> friend = userStorage.getById(friendId);
            if (friend.isPresent()) {
                friendshipStorage.remove(id, friendId);
                log.info("Пользователь с id = {} удалил друга с id = {}", id, friendId);
            } else {
                log.error(String.format(NOT_FOUND_MESSAGE, friendId));
                throw new NotFoundException(String.format(NOT_FOUND_MESSAGE, friendId));
            }
        } else {
            log.error(String.format(NOT_FOUND_MESSAGE, id));
            throw new NotFoundException(String.format(NOT_FOUND_MESSAGE, id));
        }
    }

    public List<User> findCommonFriends(Long id, Long otherId) {
        Optional<User> user = userStorage.getById(id);
        if (user.isPresent()) {
            Optional<User> otherUser = userStorage.getById(otherId);
            if (otherUser.isPresent()) {
                return userStorage.findCommonFriends(id, otherId);
            } else {
                log.error(String.format(NOT_FOUND_MESSAGE, otherId));
                throw new NotFoundException(String.format(NOT_FOUND_MESSAGE, otherId));
            }
        } else {
            log.error(String.format(NOT_FOUND_MESSAGE, id));
            throw new NotFoundException(String.format(NOT_FOUND_MESSAGE, id));
        }
    }

    private void validate(User user) throws DuplicatedDataException {
        List<User> users = userStorage.getAll();

        if (users.stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()) && !Objects.equals(u.getId(), user.getId()))) {
            log.error("Email {} уже используется", user.getEmail());
            throw new DuplicatedDataException("Этот email уже используется");
        }

        if (users.stream()
                .anyMatch(u -> u.getLogin().equals(user.getLogin()) && !Objects.equals(u.getId(), user.getId()))) {
            log.error("Логин {} уже используется", user.getLogin());
            throw new DuplicatedDataException("Этот логин уже используется");
        }

        if (user.getLogin().contains(" ")) {
            log.error("Логин {} уже содержит пробелы", user.getLogin());
            throw new ValidationException("login", "Логин не может содержать пробелы");
        }
    }

}
