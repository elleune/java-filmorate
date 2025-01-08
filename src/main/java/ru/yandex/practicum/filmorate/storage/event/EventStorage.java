package ru.yandex.practicum.filmorate.storage.event;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventStorage {

    void createEvent(Event event);

    List<Event> getEvenByUserId(long userId);
}
