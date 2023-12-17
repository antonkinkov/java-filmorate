package ru.yandex.practicum.filmorate.storage.db.film;


import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    boolean removeLike(Long filmId, Long userId);

    boolean addLike(Long filmId, Long userId);

    Film create(Film film);

    Optional<Film> update(Film film);

    List<Film> getAll();

    List<Film> getMostPopular(Integer count);

    Optional<Film> findById(Long filmId);
}
