package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private static final int DEFAULT_POPULAR_FILMS_COUNT = 10;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public void likeFilm(long filmId, long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userService.getUserById(userId);
        if (film.getIdUserLike().contains(userId)) {
            throw new ValidationException("Пользователь " + user.getName() + " уже поставил лайк фильму "
                    + film.getName() + ".");
        }
        film.getIdUserLike().add(userId);
        log.info("Пользователь {} поставил лайк фильму {}.", user.getName(), film.getName());
    }

    public void removeLikeFromFilm(long filmId, long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userService.getUserById(userId);
        film.getIdUserLike().remove(userId);
        log.info("Пользователь {} убрал лайк фильма {}.", user.getName(), film.getName());
    }

    public List<Film> getPopularFilms(Integer count) {
        Optional<Integer> optionalCount = Optional.ofNullable(count);
        log.info("Получение популярных фильмов.");
        return filmStorage.findAllFilms().stream()
                .sorted((film1, film2) -> Integer.compare(film2.getIdUserLike().size(), film1.getIdUserLike().size()))
                .limit(optionalCount.orElse(DEFAULT_POPULAR_FILMS_COUNT))
                .toList();
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film newFilm) {
        return filmStorage.updateFilm(newFilm);
    }

    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public List<Film> getTopFilms(int count) {
        Collection<Film> allFilms = filmStorage.findAllFilms();
        return allFilms.stream()
                .sorted(((o1, o2) -> o2.getIdUserLike().size() - o1.getIdUserLike().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
