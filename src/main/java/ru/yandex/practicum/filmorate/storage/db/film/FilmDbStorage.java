package ru.yandex.practicum.filmorate.storage.db.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.MapperConstants;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Film> findById(Long filmId) {
        String sqlQuery = "SELECT f.*, " +
                "m.id AS mpa_id, " +
                "m.name AS mpa_name, " +
                "g.id AS genre_id, " +
                "g.name AS genre_name " +
                "FROM films AS f " +
                "JOIN mpa_ratings AS m ON f.mpa_id = m.id " +
                "LEFT JOIN films_genres AS fg ON f.id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.id " +
                "WHERE f.id = ?";

        List<Film> res = jdbcTemplate.query(sqlQuery, new FilmMapper(), filmId);

        if (Objects.nonNull(res) && !res.isEmpty()) {
            return Optional.of(res.get(0));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Film> update(Film film) {
        String query = "UPDATE films SET " +
                "name = ?, " +
                "description = ?, " +
                "release_date = ?, " +
                "duration = ?, " +
                "mpa_id = ? " +
                "WHERE id = ?";

        jdbcTemplate.update(query, film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        return updateFilmGenres(film);
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO films (name, description, release_date, duration, mpa_id)" +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        Film newFilm = film;

        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(sqlQuery, new String[]{MapperConstants.ID.getLowerCaseName()});

            statement.setString(1, newFilm.getName());
            statement.setString(2, newFilm.getDescription());

            final LocalDate releaseDate = newFilm.getReleaseDate();
            if (isNull(releaseDate)) {
                statement.setNull(3, Types.DATE);
            } else {
                statement.setDate(3, Date.valueOf(releaseDate));
            }
            statement.setInt(4, newFilm.getDuration());
            statement.setLong(5, newFilm.getMpa().getId());

            return statement;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        Optional<Film> opFilm = updateFilmGenres(film);
        if (opFilm.isPresent()) {
            film = opFilm.get();
        }

        return film;
    }

    private Optional<Film> updateFilmGenres(Film film) {
        jdbcTemplate.update("DELETE FROM films_genres WHERE film_id = ?", film.getId());

        if (!isNull(film.getGenres()) && !film.getGenres().isEmpty()) {
            String query = "INSERT INTO films_genres (genre_id, film_id) VALUES (?, ?)";
            List<Long> genreIds = film.getGenres().stream()
                    .map(Genre::getId)
                    .distinct()
                    .collect(Collectors.toList());

            jdbcTemplate.batchUpdate(query, genreIds, 50,
                    (PreparedStatement ps, Long genreId) -> {
                        ps.setLong(1, genreId);
                        ps.setLong(2, film.getId());
                    });
        }

        return findById(film.getId());
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT f.*, " +
                "m.id AS mpa_id, " +
                "m.name AS mpa_name, " +
                "g.id AS genre_id, " +
                "g.name AS genre_name, " +
                "FROM films AS f " +
                "JOIN mpa_ratings AS m ON f.mpa_id = m.id " +
                "LEFT JOIN films_genres AS fg ON f.id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.id";

        return jdbcTemplate.query(sqlQuery, new FilmMapper());
    }

    @Override
    public boolean removeLike(Long filmId, Long userId) {

        String query = "DELETE FROM films_likes WHERE film_id = ? AND user_id = ?";

        return jdbcTemplate.update(query, filmId, userId) != 0;
    }

    @Override
    public boolean addLike(Long filmId, Long userId) {
        String query = "INSERT INTO films_likes (film_id, user_id) VALUES (?, ?)";

        return jdbcTemplate.update(query, filmId, userId) != 0;
    }

    @Override
    public List<Film> getMostPopular(Integer count) {

        String query = "SELECT *, " +
                "m.id AS mpa_id, " +
                "m.name AS mpa_name, " +
                "g.id AS genre_id, " +
                "g.name AS genre_name " +
                "FROM films AS f " +
                "LEFT JOIN " +
                "(SELECT film_id,  " +
                "COUNT(*) likes_count " +
                "FROM films_likes " +
                "GROUP BY film_id) AS l " +
                "ON f.id = l.film_id " +
                "JOIN mpa_ratings AS m ON f.mpa_id = m.id " +
                "LEFT JOIN films_genres AS fg ON f.id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.id " +
                "ORDER BY l.likes_count DESC " +
                "LIMIT ?";

        List<Film> films = jdbcTemplate.query(query, new FilmMapper(), count);

        return films;
    }
}
