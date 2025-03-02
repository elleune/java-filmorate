package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    static final String NOT_FOUND_MESSAGE = "Рейтинг с id = %s не найден";
    final MpaStorage ratingStorage;

    public List<Mpa> findAll() {
        return ratingStorage.getAll();
    }

    public Mpa findById(Long id) {
        Optional<Mpa> mpaRating = ratingStorage.getById(id);
        if (mpaRating.isPresent()) {
            return mpaRating.get();
        }
        throw new NotFoundException(String.format(NOT_FOUND_MESSAGE, id));
    }
}