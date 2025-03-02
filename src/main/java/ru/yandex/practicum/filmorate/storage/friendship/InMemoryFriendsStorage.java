package ru.yandex.practicum.filmorate.storage.friendship;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friends;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class InMemoryFriendsStorage implements FriendsStorage {
    private final Set<Friends> friends = new HashSet<>();

    @Override
    public List<Friends> findAll() {
        return new ArrayList<>(friends);
    }

    @Override
    public void create(long userId, long friendId) {
        Optional<Friends> friendshipOp = friends.stream()
                .filter(f -> f.getUserId() == userId && f.getFriendId() == friendId)
                .findFirst();

        Optional<Friends> friendFriendshipOp = friends.stream()
                .filter(f -> f.getUserId() == friendId && f.getFriendId() == userId)
                .findFirst();

        if ((friendshipOp.isEmpty())) {
            friendFriendshipOp.ifPresent(friendship -> friendship.setAccepted(true));
            if (friendFriendshipOp.isPresent()) {
                friends.add(new Friends(userId, friendId, true));
            } else {
                friends.add(new Friends(userId, friendId, false));
            }
        }
    }

    @Override
    public void remove(long userId, long friendId) {
        Optional<Friends> friendshipOp = friends.stream()
                .filter(f -> f.getUserId() == userId && f.getFriendId() == friendId)
                .findFirst();
        friendshipOp.ifPresent(friends::remove);

        friendshipOp = friends.stream()
                .filter(f -> f.getUserId() == friendId && f.getFriendId() == userId)
                .findFirst();
        friendshipOp.ifPresent(friendship -> friendship.setAccepted(false));
    }

    @Override
    public void clear() {
        friends.clear();
    }
}
