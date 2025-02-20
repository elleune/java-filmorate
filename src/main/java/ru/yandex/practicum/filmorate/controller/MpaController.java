package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaDbStorage mpaStorage;

    public MpaController(@Qualifier("mpaDbStorage") MpaDbStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @GetMapping
    public List<MpaRating> findAll() {
        return mpaStorage.findAll();
    }

    @GetMapping("/{id}")
    public MpaRating findById(@PathVariable Long id) {
        return mpaStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("MPA рейтинг не найден с id=" + id));
    }
}