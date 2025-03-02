package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dal.repository.MpaRepository;

import java.util.List;
import java.util.Optional;

@Component
@Primary
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    final MpaRepository mpaRepository;

    @Override
    public List<Mpa> getAll() {
        return mpaRepository.findAll();
    }

    @Override
    public Optional<Mpa> getById(long id) {
        return mpaRepository.findById(id);
    }
}
