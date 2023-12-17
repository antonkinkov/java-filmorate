package ru.yandex.practicum.filmorate.storage.db.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAll() {
        String query = "SELECT m.id, m.name " +
                "FROM mpa_ratings AS m";

        return jdbcTemplate.query(query, new MpaMapper());
    }

    @Override
    public Optional<Mpa> findById(Long id) {
        String query = "SELECT id, name " +
                "FROM mpa_ratings " +
                "WHERE id = ?";
        List<Mpa> results = jdbcTemplate.query(query, new MpaMapper(), id);

        return !results.isEmpty() ? Optional.of(results.get(0)) : Optional.empty();
    }
}
