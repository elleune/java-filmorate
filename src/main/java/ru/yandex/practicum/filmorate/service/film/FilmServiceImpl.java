package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;
import ru.yandex.practicum.filmorate.service.event.EventServiceImpl;
import ru.yandex.practicum.filmorate.service.user.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.List;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage jdbcFilmRepository;
    private final UserServiceImpl userServiceImpl;
    private final EventServiceImpl eventServiceImpl;

    @Autowired
    public FilmServiceImpl(FilmStorage jdbcFilmRepository, UserServiceImpl userServiceImpl, EventServiceImpl eventServiceImpl) {
        this.jdbcFilmRepository = jdbcFilmRepository;
        this.userServiceImpl = userServiceImpl;
        this.eventServiceImpl = eventServiceImpl;
    }


    @Override
    public List<Film> getPopularFilms(long count, Integer genreId, Integer year) {
        return jdbcFilmRepository.getPopularFilms(genreId, year).stream().limit(count).toList();
    }

    @Override
    public List<Film> getCommonFilms(long userId, long friendId) {
        userServiceImpl.getUserById(userId);
        userServiceImpl.getUserById(friendId);
        return jdbcFilmRepository.getCommonFilms(userId, friendId);
    }


    @Override
    public Film createFilm(Film film) {
        Validator.validateFilm(film);
        return jdbcFilmRepository.createFilm(film);
    }


    @Override
    public Film updateFilm(Film newFilm) {
        jdbcFilmRepository.getFilmById(newFilm.getId());
        Validator.validateFilm(newFilm);
        return jdbcFilmRepository.updateFilm(newFilm);
    }


    @Override
    public Film getFilmById(long id) {
        return jdbcFilmRepository.getFilmById(id);
    }


    @Override
    public List<Film> getAllFilms() {
        return jdbcFilmRepository.getAllFilms();
    }

    @Override
    public void deleteFilmById(long filmId) {
        getFilmById(filmId);
        jdbcFilmRepository.deleteFilmById(filmId);
    }


    @Override
    public void deleteAllFilms() {
        jdbcFilmRepository.deleteAllFilms();
    }


    @Override
    public void addLikeToFilm(long filmId, long userId) {
        jdbcFilmRepository.addLikeToFilm(filmId, userId);
        eventServiceImpl.createEvent(userId, EventType.LIKE, Operation.ADD, filmId);
    }


    @Override
    public void removeLikeFromFilm(long filmId, long userId) {
        jdbcFilmRepository.getFilmById(filmId);
        userServiceImpl.getUserById(userId);
        jdbcFilmRepository.removeLikeFromFilm(filmId, userId);
        eventServiceImpl.createEvent(userId, EventType.LIKE, Operation.REMOVE, filmId);
    }
}
