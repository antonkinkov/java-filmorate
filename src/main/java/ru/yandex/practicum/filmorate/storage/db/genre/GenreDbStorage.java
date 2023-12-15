package ru.yandex.practicum.filmorate.storage.db.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAll() {
        String query = "SELECT m.id, m.name " +
                "FROM genres AS m";

        return jdbcTemplate.query(query, new GenreMapper());
    }

    @Override
    public Optional<Genre> findById(Long id) {
        String query = "SELECT id, name " +
                "FROM genres " +
                "WHERE id = ?";
        List<Genre> results = jdbcTemplate.query(query, new GenreMapper(), id);

        return !results.isEmpty() ? Optional.of(results.get(0)) : Optional.empty();
    }

}
