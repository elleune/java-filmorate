package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationException extends RuntimeException {
    private final String parameter;
    private final String reason;

    public ValidationException(String parameter, String reason) {
        this.parameter = parameter;
        this.reason = reason;
    }
}