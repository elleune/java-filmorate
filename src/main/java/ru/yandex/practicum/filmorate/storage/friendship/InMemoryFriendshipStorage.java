package ru.yandex.practicum.filmorate.storage.friendship;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Optional;

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

        Optional<Friendship> friendFriendshipOp = friendships.stream()
                .filter(f -> f.getUserId() == friendId && f.getFriendId() == userId)
                .findFirst();

        if ((friendshipOp.isEmpty())) {
            friendFriendshipOp.ifPresent(friendship -> friendship.setAccepted(true));
            if (friendFriendshipOp.isPresent()) {
                friendships.add(new Friendship(userId, friendId, true));
            } else {
                friendships.add(new Friendship(userId, friendId, false));
            }
        }
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
        friendshipOp.ifPresent(friendship -> friendship.setAccepted(false));
    }

    @Override
    public void clear() {
        friendships.clear();
    }
}
