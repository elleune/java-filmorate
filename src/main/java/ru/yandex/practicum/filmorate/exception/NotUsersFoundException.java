package ru.yandex.practicum.filmorate.exception;

public class NotUsersFoundException extends RuntimeException {
    public NotUsersFoundException(String message) {
        super(message);
    }
}