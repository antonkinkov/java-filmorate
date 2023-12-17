package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.mpa.MpaStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {

    private final MpaStorage storage;

    @Override
    public List<Mpa> getAll() {
        return storage.getAll();
    }

    @Override
    public Mpa findById(Long id) {
        return storage.findById(id).orElseThrow(() -> {
            log.info("Ошибка при поиске рейтинга");
            throw new EntityNotFoundException("Рейтинг с id= " + id);
        });
    }
}
