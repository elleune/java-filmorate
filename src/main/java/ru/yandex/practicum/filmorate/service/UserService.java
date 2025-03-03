package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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

    static final String USER_NOT_FOUNT_ERROR = "Пользователь с id = %d не найден";

    private final UserStorage userStorage;

    private final FriendshipStorage friendshipStorage;

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        user = userStorage.create(user);
        return user;
    }

    public User getById(Long id) {
        Optional<User> user = userStorage.getById(id);
        if (user.isPresent()) {
            return user.get();
        }
        log.error(String.format(USER_NOT_FOUNT_ERROR, id));
        throw new NotFoundException(String.format(USER_NOT_FOUNT_ERROR, id));
    }

    public User update(User user) {

        Optional<User> optionalUser = userStorage.getById(user.getId());
        if (optionalUser.isEmpty()) {
            log.error(String.format(USER_NOT_FOUNT_ERROR, user.getId()));
            throw new NotFoundException(String.format(USER_NOT_FOUNT_ERROR, user.getId()));
        }

        user = userStorage.update(user);
        return user;
    }

    public void remove(Long id) {
        userStorage.remove(id);
    }

    public List<User> getFriends(Long userId) {
        User user = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUNT_ERROR, userId)));
        return userStorage.getFriends(user);
    }

    public List<User> getFriendsCommonOther(Long userId, Long otherUserId) {
        User user = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUNT_ERROR, userId)));
        User otherUser = userStorage.getById(otherUserId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUNT_ERROR, otherUserId)));
        return userStorage.getFriendsCommonOther(user, otherUser);
    }

    public void addFriend(Long userId, Long friendId) {
        if (Objects.equals(userId, friendId)) {
            throw new NotFoundException("Нельзя добавить в друзья самого себя");
        }

        User user = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUNT_ERROR, userId)));
        User friend = userStorage.getById(friendId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUNT_ERROR, friendId)));
        if (user.equals(friend))
            throw new NotFoundException("Невозможно добавить в друзья самого себя");
        friendshipStorage.create(user.getId(), friend.getId());
        log.info("Пользователь с id = {} добавил друга с id = {}", userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUNT_ERROR, userId)));
        User friend = userStorage.getById(friendId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUNT_ERROR, friendId)));
        friendshipStorage.remove(user.getId(), friend.getId());
        log.info("Пользователь с id = {} удалил друга с id = {}", userId, friendId);
    }
}
