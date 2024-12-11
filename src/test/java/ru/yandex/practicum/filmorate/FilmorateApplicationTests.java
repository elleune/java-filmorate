package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class FilmorateApplicationTests {

    @Autowired
    private UserController userController;

    @Autowired
    private FilmController filmController;

    @BeforeEach
    void setUpUser() {
        userController = new UserController();
    }

    @Test
    void createValidUser() {
        User user = new User();
        user.setLogin("validLogin");
        user.setEmail("email@example.com");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(2001, 11, 1));

        User createUser = userController.postUser(user);

        assertEquals(user.getLogin(), createUser.getLogin());
        assertEquals(user.getEmail(), createUser.getEmail());
        assertEquals(user.getName(), createUser.getName());
        assertEquals(user.getBirthday(), createUser.getBirthday());
    }

    @Test
    void createNotValidNameUser() {
        User user = new User();
        user.setLogin("");
        user.setEmail("email@example.com");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(2001, 11, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.postUser(user));
        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    void createNotValidEmailUser() {
        User user = new User();
        user.setLogin("validLogin");
        user.setEmail("invalidemail.com");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(2001, 11, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.postUser(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать знак `@`", exception.getMessage());
    }

    @Test
    void createNotBirthday() {
        User user = new User();
        user.setLogin("validLogin");
        user.setEmail("email@example.com");
        user.setName("Valid Name");
        user.setBirthday(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.postUser(user));
        assertEquals("День рождения не может быть в будущем.", exception.getMessage());
    }

    @BeforeEach
    void setUpFilm() {
        filmController = new FilmController();
    }

    @Test
    void testValidFilm() {
        Film film = new Film();
        film.setName("Valid Film");
        film.setDescription("This is a valid description.");
        film.setReleaseDate(LocalDate.of(2022, 3, 12));
        film.setDuration(120);

        Film createFilm = filmController.postFilm(film);

        assertEquals(film.getName(), createFilm.getName());
        assertEquals(film.getDescription(), createFilm.getDescription());
        assertEquals(film.getReleaseDate(), createFilm.getReleaseDate());
        assertEquals(film.getDuration(), createFilm.getDuration());
    }

    @Test
    void createNotNameFilm() {
        Film film = new Film();
        film.setName("");
        film.setDescription("This is a valid description."); //
        film.setReleaseDate(LocalDate.of(2022, 3, 12));
        film.setDuration(120);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.postFilm(film));
        assertEquals("Название не может быть пустым.", exception.getMessage());
    }

    @Test
    void createNotValidDescriptionFilm() {
        Film film = new Film();
        film.setName("Valid Film");
        film.setDescription("A".repeat(201)); // 201 символ
        film.setReleaseDate(LocalDate.of(2022, 3, 12));
        film.setDuration(120);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.postFilm(film));
        assertEquals("Максимальная длина описания - 200 символов.", exception.getMessage());
    }


    @Test
    void createNotValidDuration() {
        Film film = new Film();
        film.setName("Valid Film");
        film.setDescription("This is a valid description.");
        film.setReleaseDate(LocalDate.of(2022, 3, 12));
        film.setDuration(-1);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.postFilm(film));
        assertEquals("Продолжительность фильма должна быть положительным числом.", exception.getMessage());
    }

    @Test
    void createNotReleaseFilm() {
        Film film = new Film();
        film.setName("Valid Film");
        film.setDescription("This is a valid description.");
        film.setReleaseDate(LocalDate.of(1895, 11, 27));
        film.setDuration(120);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.postFilm(film));
        assertEquals("Дата реализации - не раньше 28 декабря 1895 года.", exception.getMessage());
    }
}