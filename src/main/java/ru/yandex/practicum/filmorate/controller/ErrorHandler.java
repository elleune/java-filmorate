package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.ResourceException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHappinessOverflow(ValidationException validationException) {
        log.debug("Данные запроса содержат ошибку.");
        return new ErrorResponse(validationException.getMessage());
    }

    @ExceptionHandler(ResourceException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleHappinessOverflow(ResourceException resourceException) {
        log.debug("Данные из запроса не найдены.");
        return new ErrorResponse(resourceException.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleHappinessOverflow(Throwable throwable) {
        log.debug("Непредвиденная ошибка.");
        return new ErrorResponse("Произошла непредвиденная ошибка.");
    }
}