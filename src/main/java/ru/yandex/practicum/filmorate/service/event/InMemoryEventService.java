package ru.yandex.practicum.filmorate.service.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class InMemoryEventService implements EventService {

    private final EventStorage jdbcEventStorage;

    @Autowired
    public InMemoryEventService(EventStorage jdbcEventStorage) {
        this.jdbcEventStorage = jdbcEventStorage;
    }


    @Override
    public void createEvent(long userId, EventType eventType, Operation operation, long entityId) {
        Event event = Event.builder()
                .timestamp(Instant.now().toEpochMilli())
                .userId(userId)
                .eventType(eventType)
                .operation(operation)
                .entityId(entityId)
                .build();
        jdbcEventStorage.createEvent(event);
    }

    @Override
    public List<Event> getEvenByUserId(long userId) {
        return jdbcEventStorage.getEvenByUserId(userId);
    }


}
