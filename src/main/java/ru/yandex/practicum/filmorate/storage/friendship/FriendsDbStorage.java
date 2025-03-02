package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.storage.dal.repository.FriendsRepository;

import java.util.List;

@Component
@Primary
@RequiredArgsConstructor
public class FriendsDbStorage implements FriendsStorage {
    final FriendsRepository friendsRepository;

    @Override
    public List<Friends> findAll() {
        return friendsRepository.findAll();
    }

    @Override
    public void create(long userId, long friendId) {
        friendsRepository.create(userId, friendId);
    }

    @Override
    public void remove(long userId, long friendId) {
        friendsRepository.remove(userId, friendId);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Clearing the database table is not supported");
    }
}
