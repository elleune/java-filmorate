package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
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

    @JsonIgnore
    private Set<Long> userId = new HashSet<>();
    @JsonIgnore
    private long rate = 0;

    public void addLike(long id) {
        userId.add(id);
        rate = userId.size();
    }

    public void removeLike(long id) {
        userId.remove(id);
        rate = userId.size();
    }
}