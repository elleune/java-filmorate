package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.dal.repository.FriendshipRepository;

import java.util.List;

@Component
@Primary
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage {
    final FriendshipRepository friendshipRepository;

    @Override
    public List<Friendship> findAll() {
        return friendshipRepository.findAll();
    }

    @Override
    public void create(long userId, long friendId) {
        friendshipRepository.create(userId, friendId);
    }

    @Override
    public void remove(long userId, long friendId) {
        friendshipRepository.remove(userId, friendId);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Очистка таблицы БД не поддерживается");
    }
}
