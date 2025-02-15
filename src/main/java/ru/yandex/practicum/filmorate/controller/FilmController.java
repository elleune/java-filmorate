package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;
    private Validator validator = new Validator();
    private final Integer defaultCountOfMostLikedFilms = 10;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @RequestMapping("/{id}")
    @GetMapping
    public Film findById(@PathVariable Integer id) {
        return filmService.findById(id);
    }

    @RequestMapping("/popular")
    @GetMapping
    public List<Film> findMostPopularFilms(@RequestParam(required = false) Integer count) {
        if (count == null) {
            return filmService.getMostLikedFilms(defaultCountOfMostLikedFilms);
        } else {
            return filmService.getMostLikedFilms(count);
        }
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        validator.validationFilm(film);
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        if (!filmService.checkFilmExist(film.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        }
        validator.validationFilm(film);
        return filmService.update(film);
    }

    @RequestMapping(value = "/{id}/like/{userId}", method = RequestMethod.PUT)
    public void likeFilm(@PathVariable Integer id, @PathVariable Integer userId) throws ValidationException {
        filmService.addLike(id, userId);
    }

    @RequestMapping(value = "/{id}/like/{userId}", method = RequestMethod.DELETE)
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) throws ValidationException {
        filmService.deleteLike(id, userId);
    }
}