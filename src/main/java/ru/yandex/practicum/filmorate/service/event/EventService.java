package ru.yandex.practicum.filmorate.service.event;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;

import java.util.List;

public interface EventService {

    void createEvent(long userId, EventType eventType, Operation operation, long entityId);

    List<Event> getEvenByUserId(long userId);


}
