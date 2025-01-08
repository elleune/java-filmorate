package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InMemoryGenreService implements GenreService {

    private final GenreStorage jdbcGenreRepository;

    @Override
    public Genre getGenreById(int id) {
        return jdbcGenreRepository.getGenreById(id);
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcGenreRepository.getAllGenres();
    }

    @Override
    public Set<Genre> getAllGenresForFilm(long filmId) {
        return jdbcGenreRepository.getAllGenresByFilmId(filmId).stream()
                .sorted(Comparator.comparingLong(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
