package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.mpa.MpaStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
public class MpaDbStorage {

    private final MpaStorage storage;

    @Autowired
    public MpaDbStorage(MpaStorage storage) {
        this.storage = storage;
    }

    @Test
    void findByIdTest() {
        Mpa mpa = storage.findById(1L).get();

        assertEquals("G", mpa.getName());
    }

    @Test
    void getAllTest() {
        List<Mpa> mpas = storage.getAll();

        assertEquals(5, mpas.size());
    }
}
