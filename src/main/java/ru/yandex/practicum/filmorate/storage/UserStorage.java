package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;


public interface UserStorage {

    User create(User user); // Добавить пользователя

    List<User> getAll(); // Получить всех пользователей

    Optional<User> findById(Long userId);

}
