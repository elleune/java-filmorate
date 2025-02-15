package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
@Primary
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> findAll() {
        String sql = "select * from users";
        Collection<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
        return users;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("USER_ID");
        String email = rs.getString("EMAIL");
        String name = rs.getString("NAME");
        String login = rs.getString("LOGIN");
        ;
        LocalDate rd = rs.getDate("BIRTHDAY").toLocalDate();

        String sql = "select * from FRIENDS where USER_ID=" + id;
        Collection<Integer> likess = jdbcTemplate.query(sql, (rs2, rowNum) -> makeFr(rs2));
        Set<Integer> friends = new HashSet<>(likess);

        User user = User.builder()
                .id(id).name(name).email(email).birthday(rd).login(login)
                .friends(friends)
                .build();

        return user;
    }

    private Integer makeFr(ResultSet rs) throws SQLException {
        int id = rs.getInt("FRIEND_ID");
        return id;
    }

    @Override
    public User create(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        String sqlQuery = "insert into users(EMAIL, LOGIN, NAME, BIRTHDAY) " +
                "values (?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        String sql = "select * from users where NAME='" + user.getName() + "' AND LOGIN='" + user.getLogin()
                + "' AND EMAIL='" + user.getEmail() + "'";
        int id = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getInt("USER_ID"));
        user.setId(id);

        if (user.getFriends() != null) {
            user.getFriends()
                    .stream()
                    .forEach(fr -> {
                        String sql2 = "insert into FRIENDS(USER_ID, FRIEND_ID) " +
                                "values (?, ?)";
                        jdbcTemplate.update(sql2,
                                id,
                                fr
                        );
                    });
        } else {
            user.setFriends(new HashSet<Integer>());
        }
        return user;
    }

    @Override
    public User update(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        String sqlQuery = "update users SET EMAIL='" + user.getEmail() + "', LOGIN='" + user.getLogin() + "', NAME='" + user.getName() + "', BIRTHDAY='" + user.getBirthday() + "' where USER_ID=" + user.getId();
        jdbcTemplate.update(sqlQuery);

        String sqlDel = "delete from FRIENDS where USER_ID=" + user.getId();
        jdbcTemplate.execute(sqlDel);

        if (user.getFriends() != null) {
            user.getFriends()
                    .stream()
                    .forEach(fr -> {
                        String sql2 = "insert into FRIENDS(USER_ID, FRIEND_ID) " +
                                "values (?, ?)";
                        jdbcTemplate.update(sql2,
                                user.getId(),
                                fr
                        );
                    });
        } else {
            user.setFriends(new HashSet<>());
        }
        return user;
    }

    @Override
    public User findUserById(Integer id) {
        String sql = "select * from users where USER_ID=" + id;
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("Пользователя с таким id не существует");
        } else {
            return users.getFirst();
        }
    }

    public boolean checkUserExist(Integer id) {
        String sql = "select * from users where USER_ID=" + id;
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
        return !users.isEmpty();
    }
}