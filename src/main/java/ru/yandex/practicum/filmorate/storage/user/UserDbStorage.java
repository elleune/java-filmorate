package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.constants.ConstantSqlStorage.ALL_FROM_CLIENTS;

@Slf4j
@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final Validator validator = new Validator();

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query(
                ALL_FROM_CLIENTS,
                (rs, rowNum) ->
                        new User(
                                rs.getLong("CLIENT_ID"),
                                rs.getString("CLIENT_EMAIL"),
                                rs.getString("LOGIN"),
                                rs.getString("CLIENT_NAME"),
                                rs.getDate("BIRTHDAY").toLocalDate()
                        )
        );
    }

    @Override
    public User create(User user) {
        validator.validationUser(user);
        String sqlQuery = "insert into CLIENTS(CLIENT_ID, CLIENT_EMAIL, LOGIN, CLIENT_NAME, BIRTHDAY) " +
                "values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        return user;
    }

    @Override
    public User update(User user) {
        validator.validationUser(user);
        String sqlQuery = "update CLIENTS set " +
                "CLIENT_EMAIL = ?, LOGIN = ?, CLIENT_NAME = ?, BIRTHDAY = ?" +
                "where CLIENT_ID = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public void delete(Long id) {
        String sqlQuery = "delete from CLIENTS where CLIENT_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Optional<User> getUserById(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(ALL_FROM_CLIENTS + " where CLIENT_ID = ?", id);
        if (userRows.next()) {
            User user = new User(
                    userRows.getLong("CLIENT_ID"),
                    userRows.getString("CLIENT_EMAIL"),
                    userRows.getString("LOGIN"),
                    userRows.getString("CLIENT_NAME"),
                    userRows.getDate("BIRTHDAY").toLocalDate()
            );
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public void addFriend(long id, long friendId) {
        String sqlQuery = "insert into FRIENDSHIP(FRIEND1_ID, FRIEND2_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                id,
                friendId
        );
    }

    @Override
    public List<User> getFriendsList(long id) {
        String sql = "SELECT CL.CLIENT_ID, CL.CLIENT_EMAIL, CL.LOGIN, CL.CLIENT_NAME, CL.BIRTHDAY " +
                "FROM CLIENTS CL JOIN\n" +
                "(SELECT FRIEND2_ID FROM CLIENTS C join FRIENDSHIP F on C.CLIENT_ID = F.FRIEND1_ID " +
                "WHERE CLIENT_ID = ?) S\n" +
                "    ON S.FRIEND2_ID = CL.CLIENT_ID;";
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) ->
                        new User(
                                rs.getLong("CLIENT_ID"),
                                rs.getString("CLIENT_EMAIL"),
                                rs.getString("LOGIN"),
                                rs.getString("CLIENT_NAME"),
                                rs.getDate("BIRTHDAY").toLocalDate()
                        ), id
        );
    }

    public void deleteFriend(long id, long friendId) {
        String sqlQuery = "delete from FRIENDSHIP where FRIEND1_ID = ? AND FRIEND2_ID = ? " +
                "OR FRIEND1_ID = ? AND FRIEND2_ID =?";
        jdbcTemplate.update(sqlQuery, id, friendId, friendId, id);
    }
}