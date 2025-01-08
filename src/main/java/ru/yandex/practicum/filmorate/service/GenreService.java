package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.genry.GenreDaoImpl;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class GenreService {
    private final GenreDaoImpl genreDaoimpl;

    @Autowired
    public GenreService(GenreDaoImpl genreDaoimpl) {
        this.genreDaoimpl = genreDaoimpl;
    }

    public Optional<Genre> getGenreById(long id) {
        if (genreDaoimpl.getGenreById(id).isEmpty()) {
            throw new DataNotFoundException("Нет такого id");
        }
        return genreDaoimpl.getGenreById(id);
    }

    public Collection<Genre> findAll() {
        return genreDaoimpl.getAllGenre();
    }
}