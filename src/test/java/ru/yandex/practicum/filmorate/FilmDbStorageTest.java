package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
public class FilmDbStorageTest {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmDbStorageTest(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Test
    @Order(10)
    void createAndGetAllFilmTest() {
        filmStorage.create(Film.builder()
                .name("nisi eiusmod")
                .duration(100)
                .description("adipisicing")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(Mpa.builder()
                        .id(1L)
                        .build())
                .build());
        assertEquals(1, filmStorage.getAll().size());
    }

    @Test
    @Order(20)
    void getByUdTest() {
        Film film = filmStorage.create(Film.builder()
                .name("nisi eiusmod")
                .duration(100)
                .description("adipisicing")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(Mpa.builder()
                        .id(1L)
                        .build())
                .build());

        Optional<Film> filmOptional = filmStorage.findById(film.getId());

        assertThat(filmOptional).isPresent()
                .hasValueSatisfying(f -> assertThat(f)
                        .hasFieldOrPropertyWithValue("name", "nisi eiusmod"));
    }

    @Test
    @Order(20)
    void getPopularTest() {
        Film film = filmStorage.create(Film.builder()
                .id(2L)
                .name("nisi eiusmod")
                .duration(100)
                .description("adipisicing")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(Mpa.builder()
                        .id(1L)
                        .build())
                .build());

        List<Film> films = filmStorage.getMostPopular(1);

        assertEquals(1, films.size());
    }

    @Test
    @Order(20)
    void updateTest() {
        Film film = filmStorage.create(Film.builder()
                .name("nisi eiusmod")
                .duration(100)
                .description("adipisicing")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(Mpa.builder()
                        .id(1L)
                        .build())
                .build());

        film.setName("nisi eiusmod updated");
        filmStorage.update(film);

        Optional<Film> filmOptional = filmStorage.findById(film.getId());

        assertThat(filmOptional).isPresent()
                .hasValueSatisfying(f -> assertThat(f)
                        .hasFieldOrPropertyWithValue("name", "nisi eiusmod updated"));
    }

}
