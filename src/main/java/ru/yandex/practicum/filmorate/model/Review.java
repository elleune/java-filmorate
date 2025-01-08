package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Review {
    private Long reviewId;
    @NotNull(message = "content не может быть пустым.")
    private String content;
    @NotNull(message = "isPositive не может быть пустым.")
    private Boolean isPositive;
    @NotNull(message = "userId не может быть пустым.")
//    @Min(value = 1, message = "Идентификатор пользователя должен быть больше 0.")
    private Long userId;
    @NotNull(message = "filmId не может быть пустым.")
//    @Min(value = 1, message = "Идентификатор фильма должен быть больше 0.")
    private Long filmId;
    private Integer useful;
}
