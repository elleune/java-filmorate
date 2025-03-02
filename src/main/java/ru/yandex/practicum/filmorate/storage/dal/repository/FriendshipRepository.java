package ru.yandex.practicum.filmorate.storage.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

@Repository
public class FriendshipRepository extends BaseRepository<Friendship> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM friendships";
    private static final String INSERT_QUERY = "INSERT INTO friendships (user_id, friend_id, is_accepted) " +
            "SELECT ?, ?, (SELECT count(*) FROM friendships WHERE user_id = ? AND friend_id = ?) FROM dual";
    private static final String ACCEPT_QUERY = "UPDATE friendships SET is_accepted = 1 WHERE user_id = ? AND friend_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
    private static final String REMOVE_ACCEPT_QUERY = "UPDATE friendships SET is_accepted = false WHERE user_id = ? AND friend_id = ?";

    public FriendshipRepository(JdbcTemplate jdbc, RowMapper<Friendship> mapper) {
        super(jdbc, mapper);
    }

    public List<Friendship> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public void create(long userId, long friendId) {
        insert(INSERT_QUERY,
                userId,
                friendId,
                friendId,
                userId);
        update(ACCEPT_QUERY, friendId, userId);
    }

    public void remove(long userId, long friendId) {
        delete(DELETE_QUERY, userId, friendId);
        update(REMOVE_ACCEPT_QUERY, friendId, userId);
    }

}

