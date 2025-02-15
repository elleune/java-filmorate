package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    @Builder.Default
    private Set<Integer> likes = new HashSet<Integer>();
    @Builder.Default
    private List<Genres> genres = new ArrayList<>();
    private MPA mpa;

    public void setLike(Integer id) {
        if (likes == null) {
            likes = new HashSet<>();
        }
        likes.add(id);
    }

    public void deleteLike(Integer id) {
        if (likes.contains(id)) {
            likes.remove(id);
        }
    }
}