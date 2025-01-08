package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotUsersFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;
import ru.yandex.practicum.filmorate.service.event.InMemoryEventService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class InMemoryUserService implements UserService {
    private final UserStorage jdbcUserRepository;
    private final InMemoryEventService eventServiceImpl;
    private static final String CONFIRMED = "CONFIRMED";
    private static final String UNCONFIRMED = "UNCONFIRMED";


    @Autowired
    public InMemoryUserService(UserStorage jdbcUserRepository, InMemoryEventService eventServiceImpl) {
        this.jdbcUserRepository = jdbcUserRepository;
        this.eventServiceImpl = eventServiceImpl;
    }

    public User createUser(User user) {
        Validator.validateUser(user);
        User userResult = jdbcUserRepository.createUser(user);
        log.info("Создан пользователь:\n{}", userResult);
        return userResult;
    }


    public User updateUser(User user) {
        User oldUser = jdbcUserRepository.getUserById(user.getId());
        log.info("Старый пользователь:\n{}", oldUser);
        Validator.validateUser(user);
        User userResult = jdbcUserRepository.updateUser(user);
        return userResult;
    }


    public void addFriend(long userId, long friendId) {
        getUserById(userId);
        User userFriend = getUserById(friendId);
        String status = UNCONFIRMED;
        if (userFriend.getFriendsId().contains(userId)) {
            status = CONFIRMED;
            jdbcUserRepository.updateStatus(friendId, userId, status);
        }
        jdbcUserRepository.addFriend(userId, friendId, status);
        eventServiceImpl.createEvent(userId, EventType.FRIEND, Operation.ADD, friendId);
    }


    public void removeFriend(long userId, long friendId) {
        User user = getUserById(userId);
        User userFriend = getUserById(friendId);
        user.getFriendsId().remove(friendId);
        if (userFriend.getFriendsId().contains(userId)) {
            jdbcUserRepository.updateStatus(friendId, userId, UNCONFIRMED);
        }
        jdbcUserRepository.removeFriend(userId, friendId);
        eventServiceImpl.createEvent(userId, EventType.FRIEND, Operation.REMOVE, friendId);
    }


    public User getUserById(long userId) {
        return jdbcUserRepository.getUserById(userId);
    }

    public List<User> getAllUsers() {
        List<User> users = jdbcUserRepository.getAllUsers();
        if (users.isEmpty()) {
            throw new NotUsersFoundException("Список пользователей пуст.");
        }
        return users;
    }

    public List<User> getUserFriends(long userId) {
        jdbcUserRepository.getUserById(userId);
        return jdbcUserRepository.getAllFriends(userId);
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        User user = getUserById(userId);
        User userOther = getUserById(otherId);
        List<User> commonFriends = new ArrayList<>();
        for (Long id : user.getFriendsId()) {
            if (userOther.getFriendsId().contains(id)) {
                commonFriends.add(getUserById(id));
            }
        }
        return commonFriends;
    }


    public void deleteUserById(long userId) {
        getUserById(userId);
        jdbcUserRepository.deleteUserById(userId);
    }

    public void deleteAllUsers() {
        jdbcUserRepository.deleteAllUsers();
    }
}
