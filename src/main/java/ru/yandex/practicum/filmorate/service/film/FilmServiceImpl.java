package ru.yandex.practicum.filmorate.service.film;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.db.film.FilmStorage;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;

    private final UserService userService;


    @Override
    public boolean addLike(Long id, Long userId) {
        validateFilmId(id);
        userService.findById(userId);
        return filmStorage.addLike(id, userId);
    }

    @Override
    public boolean deleteLike(Long filmId, Long userId) {
        validateFilmId(filmId);
        userService.findById(userId);

        return filmStorage.removeLike(filmId, userId);
    }

    @Override
    public Film create(@NonNull Film film) {
        Film createdFilm = filmStorage.create(film);
        log.info("Фильм успешно добавлен.");
        return createdFilm;
    }

    @Override
    public Film update(Film film) {
        validateFilmId(film.getId());

        Film updatedFilm = filmStorage.update(film).orElseThrow(() -> {
            log.error("Ошибка при обновлении фильма.");
            return new EntityNotFoundException("Ошибка при обновлении фильма с id = " + film.getId());
        });

        log.info("Данные фильма успешно обновлены.");

        return updatedFilm;
    }

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Film findById(Long filmId) {
        return validateFilmId(filmId);
    }

    @Override
    public List<Film> getMostPopular(Integer count) {
        return filmStorage.getMostPopular(count);

    }

    private Film validateFilmId(Long id) {
        return filmStorage.findById(id)
                .orElseThrow(() -> {
                    log.error("Ошибка при валидации фильма.");
                    return new EntityNotFoundException("Фильм с id=" + id + "не найден.");
                });
    }

}