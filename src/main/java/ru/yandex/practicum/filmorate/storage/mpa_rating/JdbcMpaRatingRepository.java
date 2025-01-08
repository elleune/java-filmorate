package ru.yandex.practicum.filmorate.storage.mpa_rating;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;

@Slf4j
@Repository
public class JdbcMpaRatingRepository extends BaseRepository<Mpa> implements MpaRatingStorage {

    public JdbcMpaRatingRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    private static final String FIND_ALL_QUERY = "SELECT * from mpa_rating";
    private static final String FIND_NAME_BY_ID_QUERY = "SELECT * FROM mpa_rating WHERE rating_id = ?";

    @Override
    public List<Mpa> getAllMpas() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Mpa getMpaById(int id) {
        Mpa mpa = findOne(FIND_NAME_BY_ID_QUERY, id).orElseThrow(() -> {
            String errorMessage = "Рейтинга с ID - " + id + " не существует";
            return new NotFoundException(errorMessage);
        });
        return mpa;
    }
}
