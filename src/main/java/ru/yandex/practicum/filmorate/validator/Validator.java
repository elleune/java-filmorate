package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class Validator {
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public Validator(MpaDbStorage mpaDbStorage, GenreDbStorage genreDbStorage) {

        this.mpaDbStorage = mpaDbStorage;
        this.genreDbStorage = genreDbStorage;
    }

    public void validationUser(User user) {
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Ошибка валидации логина");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Ошибка валидации электронной почты");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Электронная почта не может быть пустой и должна содержать знак `@`");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.error("Имя пользователя пусто - используется логин в качестве имени");
            user.setName(user.getLogin());
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Некорректная дата");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "День рождения не может быть в будущем.");
        }
    }

    public void validationFilm(Film film) {
        if (film == null) {
            log.error("Фильм не может быть null");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Фильм не может быть null");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Ошибка валидации названия фильма");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Название не может быть пустым");
        }
        if (film.getDescription() == null || film.getDescription().isBlank() || film.getDescription().length() > 200) {
            log.error("Ошибка валидации описания фильма");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Максимальная длина описания - 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Ошибка валидации даты создания");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Дата реализации - не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.error("Ошибка валидации продолжительности фильма");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Продолжительность фильма должна быть положительным числом");
        }
        if (film.getMpa() == null || film.getMpa().getId() == null) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Рейтинг MPA не может быть null");
        }

        mpaDbStorage.findById(film.getMpa().getId())
                .orElseThrow(() -> new NotFoundException("MPA рейтинг не найден с id=" + film.getMpa().getId()));

        if (film.getGenres() == null) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Жанры не могут быть null");
        }
        List<Long> genreIds = film.getGenres().stream()
                .map(Genre::getId)
                .distinct()
                .collect(Collectors.toList());
        List<Genre> existingGenres = genreDbStorage.findByIds(genreIds);
        if (existingGenres.size() != genreIds.size()) {
            Set<Long> existingIds = existingGenres.stream()
                    .map(Genre::getId)
                    .collect(Collectors.toSet());
            for (Long id : genreIds) {
                if (!existingIds.contains(id)) {
                    throw new NotFoundException("Жанр не найден с id=" + id);
                }
            }
        }
    }
}