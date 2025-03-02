package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
    Long id;

    @NotNull(message = "Не передан параметр email")
    @NotBlank(message = "Email должен быть не пустым")
    @Email(message = "Email неверный формат")
    String email;

    @NotNull(message = "Не передан параметр логина")
    @NotBlank(message = "Логин не должен быть пустым")
    String login;

    String name;

    @PastOrPresent(message = "День рождения не может быть в будущем")
    LocalDate birthday;
}