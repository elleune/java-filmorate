package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;


@Data
@EqualsAndHashCode(of = {"name", "releaseDate", "mpa", "genres"})
@Builder
public class Film {
    public static final LocalDate CINEMA_BIRTH_DAY = LocalDate.of(1895, 12, 28);

    Long id;
    @NotNull(message = "Не присвоено название фильма")
    @NotBlank(message = "Название не может быть пустым")
    String name;

    @Size(max = 200, message = "Длина названия не должна превышать 200 символов")
    String description;
    LocalDate releaseDate;

    @Positive(message = "Длительность не может быть отрицательной")
    Integer duration;
    Mpa mpa;

    List<Genre> genres;
}