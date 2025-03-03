package ru.yandex.practicum.filmorate.storage.friendship;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class InMemoryFriendshipStorage implements FriendshipStorage {
    private final Set<Friendship> friendships = new HashSet<>();

    @Override
    public List<Friendship> findAll() {
        return new ArrayList<>(friendships);
    }

    @Override
    public void create(long userId, long friendId) {
        Optional<Friendship> friendshipOp = friendships.stream()
                .filter(f -> f.getUserId() == userId && f.getFriendId() == friendId)
                .findFirst();

        if ((friendshipOp.isEmpty())) {
            friendships.add(new Friendship(userId, friendId, false));
            friendships.add(new Friendship(friendId, userId, false));
        }
         friendshipOp.ifPresent(friendships::add);
    }


    @Override
    public void remove(long userId, long friendId) {
        Optional<Friendship> friendshipOp = friendships.stream()
                .filter(f -> f.getUserId() == userId && f.getFriendId() == friendId)
                .findFirst();
        friendshipOp.ifPresent(friendships::remove);

        friendshipOp = friendships.stream()
                .filter(f -> f.getUserId() == friendId && f.getFriendId() == userId)
                .findFirst();
        friendshipOp.ifPresent(friendships::remove);
    }

    @Override
    public void clear() {
        friendships.clear();
    }
}
