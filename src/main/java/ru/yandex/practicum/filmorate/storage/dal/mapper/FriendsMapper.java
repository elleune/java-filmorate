package ru.yandex.practicum.filmorate.storage.dal.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friends;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendsMapper implements RowMapper<Friends> {
    @Override
    public Friends mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Friends.builder()
                .userId(resultSet.getLong("user_id"))
                .friendId(resultSet.getLong("friend_id"))
                .isAccepted(resultSet.getBoolean("is_accepted"))
                .build();
    }
}