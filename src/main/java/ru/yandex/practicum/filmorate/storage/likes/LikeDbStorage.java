package ru.yandex.practicum.filmorate.storage.likes;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.dal.repository.LikeRepository;

import java.util.List;

@Component
@Primary
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {
    final LikeRepository likeRepository;

    @Override
    public List<Like> findAll() {
        return likeRepository.findAll();
    }

    @Override
    public void create(long userId, long filmId) {
        likeRepository.create(userId, filmId);
    }

    @Override
    public void remove(long userId, long filmId) {
        likeRepository.remove(userId, filmId);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Clearing the database table is not supported");
    }
}
