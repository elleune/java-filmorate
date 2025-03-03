package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dal.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User create(User user) {
        return userRepository.create(user);
    }

    @Override
    public User update(User user) {
        return userRepository.update(user);
    }

    @Override
    public void remove(Long userId) {
        userRepository.remove(userId);
    }

    @Override
    public List<User> getFriends(User user) {
        return userRepository.findFriendsById(user.getId());
    }

    @Override
    public List<User> getFriendsCommonOther(User user, User otherUser) {
        return userRepository.findCommonFriends(user.getId(), otherUser.getId());
    }

}
