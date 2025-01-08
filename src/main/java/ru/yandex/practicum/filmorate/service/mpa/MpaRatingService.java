package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa_rating.MpaRatingStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaRatingService implements RatingService {
    private final MpaRatingStorage jdbcMpaRatingRepository;


    @Override
    public List<Mpa> getAll() {
        return jdbcMpaRatingRepository.getAllMpas();
    }

    @Override
    public Mpa getMpaById(int id) {
        return jdbcMpaRatingRepository.getMpaById(id);
    }

}
