package ru.yandex.practicum.filmorate.storage.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;

@Repository
@Slf4j
public class JdbcEventStorage extends BaseRepository<Event> implements EventStorage {

    public JdbcEventStorage(JdbcTemplate jdbc, RowMapper<Event> mapper) {
        super(jdbc, mapper);
    }
    
    @Override
    public void createEvent(Event event) {
        String CREATE_EVENT_QUERY = """
                INSERT INTO events (TIMESTAMP,USER_ID,EVENT_TYPE,OPERATION,ENTITY_ID)
                VALUES (?, ?, ?, ?, ?)
                """;
        long id = insert(CREATE_EVENT_QUERY,
                event.getTimestamp(),
                event.getUserId(),
                event.getEventType().toString(),
                event.getOperation().toString(),
                event.getEntityId());
        event.setEventId(id);
    }

    @Override
    public List<Event> getEvenByUserId(long userId) {
        String GET_EVENT_BY_USER_ID_QUERY = """
                SELECT *
                FROM events
                WHERE user_id = ?
                """;
        return findMany(GET_EVENT_BY_USER_ID_QUERY, userId);
    }
}
