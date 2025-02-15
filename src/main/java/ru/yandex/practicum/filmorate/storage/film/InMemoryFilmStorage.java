package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.Collection;
import java.util.HashMap;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film findById(Integer id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new ResourceNotFoundException("Фильма с таким id не существует");
        }
    }

    @Override
    public boolean checkFilmExist(Integer id) {
        return true;
    }

    @Override
    public Film create(Film film) throws ValidationException {
        Validator.validationFilm(film);
        id++;
        film.setId(id);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) throws ValidationException {
        Validator.validationFilm(film);
        if (films.containsKey(film.getId())) {
            Film tmpFilm = films.get(film.getId());
            tmpFilm.setName(film.getName());
            tmpFilm.setDescription(film.getDescription());
            tmpFilm.setReleaseDate(film.getReleaseDate());
            tmpFilm.setDuration(film.getDuration());
            films.replace(tmpFilm.getId(), tmpFilm);
            film = tmpFilm;
        } else {
            throw new ResourceNotFoundException("Фильма с таким id не существует");
        }
        return film;
    }
}