package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationFilmTests {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @Test
    void shouldTrueCreateFilm() {
        Film film = Film.builder()
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate(LocalDate.of(1967,3,25))
                .duration(100)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFalseCreateFilmFailName() {
        Film film = Film.builder()
                .name("")
                .description("Description")
                .releaseDate(LocalDate.of(1900,3,25))
                .duration(200)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFalseCreateFilmFailDescription() {
        Film film = Film.builder()
                .name("Film name")
                .description("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль." +
                        " Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги," +
                        " а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.")
                .releaseDate(LocalDate.of(1900,3,25))
                .duration(200)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFalseCreateUserFilmReleaseDate() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(1890,3,25))
                .duration(200)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }


    @Test
    void shouldFalseCreateUserFilmDuration() {
        Film film = Film.builder()
                .name("Name")
                .description("Descrition")
                .releaseDate(LocalDate.of(1980,3,25))
                .duration(-200)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }
}