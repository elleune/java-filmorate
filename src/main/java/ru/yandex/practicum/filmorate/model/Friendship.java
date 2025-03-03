package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(exclude = "isConfirmed")
@Builder
public class Friendship {
    Long userId;
    Long friendId;
    boolean isConfirmed;
}
