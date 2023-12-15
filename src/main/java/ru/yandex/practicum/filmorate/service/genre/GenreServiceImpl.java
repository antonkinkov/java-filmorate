package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.genre.GenreStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreStorage storage;

    @Override
    public List<Genre> getAll() {
        return storage.getAll();
    }

    @Override
    public Genre findById(Long id) {
        return storage.findById(id).orElseThrow(() -> {
            log.info("Ошибка при поиске рейтинга");
            throw new EntityNotFoundException("Рейтинг с id= " + id);
        });
    }
}
