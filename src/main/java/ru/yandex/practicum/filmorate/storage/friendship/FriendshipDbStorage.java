package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.dal.repository.FriendshipRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage {

    final FriendshipRepository friendshipRepository;

    @Override
    public List<Friendship> getAll() {
        return friendshipRepository.findAll();
    }

    @Override
    public void create(Long userId, Long friendId) {
        friendshipRepository.create(userId, friendId);
    }

    @Override
    public void remove(Long userId, Long friendId) {
        friendshipRepository.remove(userId, friendId);
    }

}
