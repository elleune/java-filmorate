package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film {

    private Long id;
    private String name;
    private LocalDate releaseDate;
    private String description;
    private long duration;
    private Mpa mpa;
    @Builder.Default
    private Set<Long> likes = new HashSet<>();
    @Builder.Default
    private List<Genre> genres = new ArrayList<>();
    @Builder.Default
    private long rate = 0;

    public Film(Long id, String name, LocalDate releaseDate, String description, long duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.description = description;
        this.duration = duration;
        this.mpa = mpa;
    }
}