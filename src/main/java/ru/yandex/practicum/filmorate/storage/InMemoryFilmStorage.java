package ru.yandex.practicum.filmorate.storage;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.GenerateIdentifier;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {


    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Optional<Film> findById(Long filmId) {
        if (films.containsKey(filmId)) {
            return Optional.of(films.get(filmId));
        }
        return Optional.empty();
    }

    @Override
    public Film create(Film film) {
        film.setId(GenerateIdentifier.INSTANCE.generateId(Film.class));
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }
}