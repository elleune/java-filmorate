package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;

@Slf4j
@Service
public class Validator {
    public static void validationUser(User user) {
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Ошибка валидации логина");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Ошибка валидации электронной почты");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать знак `@`");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.error("Имя пользователя пусто - используется логин в качестве имени");
            user.setName(user.getLogin());
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Некорректная дата");
            throw new ValidationException("День рождения не может быть в будущем.");
        }
    }

    public static void validationFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Ошибка валидации фильма");
            throw new ValidationException("Название не может быть пустым.");
        }
        if (film.getDescription() == null || film.getDescription().isBlank() || film.getDescription().length() > 200) {
            log.error("Ошибка валидации описания");
            throw new ValidationException("Максимальная длина описания - 200 символов.");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Ошибка валидации даты создания");
            throw new ValidationException("Дата реализации - не раньше 28 декабря 1895 года.");
        }
        if (film.getDuration().toMinutes() < Duration.ZERO.toMinutes()) {
            log.error("Ошибка валидации продолжительности фильма");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }
    }
}
