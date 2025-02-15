package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private Integer id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    @Builder.Default
    private Set<Integer> friends = new HashSet<Integer>();

    public void addFriend(Integer id) {
        if (friends == null) {
            friends = new HashSet<>();
        }
        friends.add(id);
    }

    public void deleteFriend(Integer id) {
        friends.remove(id);
    }
}