package ru.yandex.practicum.filmorate.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private static final Comparator<Film> COMPARATOR_LIKES = (curFilm, nextFilm) -> nextFilm.getLikes().size() - curFilm.getLikes().size();

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    private final UserService userService;

    @Override
    public boolean addLike(Long id, Long userId) {
        Film film = validateFilmId(id);
        userService.findById(userId);
        film.getLikes().add(userId);
        return true;
    }

    @Override
    public boolean deleteLike(Long filmId, Long userId) {
        Film film = validateFilmId(filmId);

        if (film.getLikes().isEmpty()) {
            log.error("Ошибка при удалении лайку у фильма");
            throw new EntityNotFoundException("У данного фильма нет лайков");
        }

        return film.getLikes().remove(userId);
    }

    @Override
    public Film addFilm(@NonNull Film film) {
        log.debug("Фильм успешно добавлен.");
        return filmStorage.create(film);
    }

    @Override
    public Film update(Film film) {
        Film curFilm = validateFilmId(film.getId());

        curFilm.setName(film.getName());
        curFilm.setDuration(film.getDuration());
        curFilm.setDescription(film.getDescription());
        curFilm.setReleaseDate(film.getReleaseDate());

        log.debug("Данные фильма успешно обновлены.");
        return curFilm;

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
        List<Film> films = filmStorage.getAll();
        films.sort(COMPARATOR_LIKES);

        return films.stream().limit(count).collect(Collectors.toList());

    }

    private Film validateFilmId(Long id) {
        return filmStorage.findById(id)
                .orElseThrow(() -> {
                    log.error("Ошибка при валидации фильма.");
                    return new EntityNotFoundException("Фильм с id=" + id + "не найден.");
                });
    }

}