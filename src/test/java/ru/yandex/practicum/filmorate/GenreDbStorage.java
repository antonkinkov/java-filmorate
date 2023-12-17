package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.genre.GenreStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
public class GenreDbStorage {

    private final GenreStorage genreStorage;

    @Autowired
    public GenreDbStorage(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @Test
    void findByIdTest() {
        Genre genre = genreStorage.findById(1L).get();

        assertEquals("Комедия", genre.getName());
    }

    @Test
    void getAllTest() {
        List<Genre> genres = genreStorage.getAll();

        assertEquals(6, genres.size());
    }


}
