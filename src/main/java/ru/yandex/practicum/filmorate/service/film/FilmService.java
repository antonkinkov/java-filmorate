package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {


    boolean addLike(Long userId, Long filmId);

    boolean deleteLike(Long filmId, Long userId);

    Film create(Film film);

    Film update(Film film);

    List<Film> getAll();

    List<Film> getMostPopular(Integer count);

    Film findById(Long filmId);
}
