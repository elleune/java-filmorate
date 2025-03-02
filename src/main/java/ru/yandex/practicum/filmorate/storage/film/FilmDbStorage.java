package ru.yandex.practicum.filmorate.storage.film;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dal.repository.FilmRepository;

import java.util.List;
import java.util.Optional;

@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    final FilmRepository filmRepository;

    @Override
    public List<Film> getAll() {
        return filmRepository.findAll();
    }

    @Override
    public Optional<Film> getById(long id) {
        return filmRepository.findById(id);
    }

    @Override
    public List<Film> getPopular(int count) {
        return filmRepository.findPopular(count);
    }

    @Override
    public Film create(Film film) {
        return filmRepository.create(film);
    }

    @Override
    public Film update(Film film) {
        return filmRepository.update(film);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Clearing the database table is not supported");
    }
}