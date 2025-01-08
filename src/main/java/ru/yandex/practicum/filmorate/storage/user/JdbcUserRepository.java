package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Slf4j
public class JdbcUserRepository extends BaseRepository<User> implements UserStorage {

    public JdbcUserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    private static final String CREATE_USER_QUERY = """
            INSERT INTO users(email,login,name,birthday)
            VALUES (?,?,?,?)
            """;
    private static final String UPDATE_USER_QUERY = """
            UPDATE users
            SET email = ?, login = ?, name = ?, birthday = ?
            WHERE user_id = ?
            """;
    private static final String GET_USER_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String ADD_FRIEND_QUERY = "INSERT INTO friends (user_id,friend_id,status) VALUES (?,?,?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String DELETE_ALL_USERS_QUERY = "DELETE FROM users";
    private static final String DELETE_BY_ID_USER_QUERY = "DELETE FROM users WHERE user_id = ?";
    private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM users";

    private static final String GET_ID_USER_FRIENDS_QUERY = "SELECT friend_id FROM friends WHERE user_id = ?";

    private static final String UPDATE_STATUS_QUERY = """
            UPDATE friends
            SET status = ?
            WHERE user_id = ? AND friend_id = ?
            """;
    private static final String FIND_ALL_FRIENDS_USER_QUERY = """
            SELECT u.*
            FROM users u
            JOIN friends f ON u.user_id = f.friend_id
            WHERE f.user_id = ?
            """;

    @Override
    public User createUser(User user) {
        long id = insert(CREATE_USER_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        update(UPDATE_USER_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public void addFriend(long userId, long friendId, String status) {
        add(ADD_FRIEND_QUERY, userId, friendId, status);
    }

    @Override
    public User getUserById(long userId) {
        User user = findOne(GET_USER_BY_ID_QUERY, userId).orElseThrow(() -> {
            String errorMessage = "Пользователя с ID - " + userId + " не существует.";
            return new NotFoundException(errorMessage);
        });
        user.setFriendsId(getIdUserFriends(userId));
        return user;
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        delete(DELETE_FRIEND_QUERY, userId, friendId);
    }

    @Override
    public List<User> getAllFriends(long userId) {
        List<User> users = findMany(FIND_ALL_FRIENDS_USER_QUERY, userId);
        for (User user : users) {
            user.setFriendsId(getIdUserFriends(user.getId()));
        }
        return users;
    }


    @Override
    public void deleteAllUsers() {

    }

    @Override
    public void deleteUserById(long userId) {
        delete(DELETE_BY_ID_USER_QUERY, userId);
    }


    @Override
    public List<User> getAllUsers() {
        List<User> users = findMany(FIND_ALL_USERS_QUERY);
        for (User user : users) {
            user.setFriendsId(getIdUserFriends(user.getId()));
        }
        return users;
    }

    @Override
    public void updateStatus(long userId, long friendId, String status) {
        update(UPDATE_STATUS_QUERY, status, userId, friendId);
    }


    private Set<Long> getIdUserFriends(long userId) {
        Set<Long> idFriends = new HashSet<>(findManyFriendsId(GET_ID_USER_FRIENDS_QUERY, userId));
        return idFriends;
    }

}
