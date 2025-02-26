package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = "id")
public class Film {
    private Long id;
    private String name;

    @Size
    private String description;

    @NotNull
    @PastOrPresent
    private LocalDate releaseDate;
    private int duration;

    @NotNull
    private MpaRating mpa;

    @NotNull
    private Set<Genre> genres = new HashSet<>();
}