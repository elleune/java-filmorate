package ru.yandex.practicum.filmorate.dao.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.constants.ConstantSqlStorage.ALL_FROM_MPA;

@Slf4j
@Component
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Mpa> getMpaById(long id) {
        log.debug("Получен запрос GET/mpa/{id}");
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(ALL_FROM_MPA + " WHERE MPA_ID = ?", id);
        if (mpaRows.next()) {
            Mpa mpa = new Mpa(
                    mpaRows.getInt("MPA_ID"),
                    mpaRows.getString("MPA_INFO")
            );
            return Optional.of(mpa);
        } else {
            log.info("Данные с идентификатором {} не найдены.", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        log.debug("Получен запрос GET/mpa");
        return jdbcTemplate.query(
                ALL_FROM_MPA,
                (rs, rowNum) ->
                        new Mpa(
                                rs.getInt("MPA_ID"),
                                rs.getString("MPA_INFO")
                        )
        );
    }
}