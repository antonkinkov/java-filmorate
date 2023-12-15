package ru.yandex.practicum.filmorate.storage.db.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;


public interface UserStorage {

    User create(User user); // Добавить пользователя

    List<User> getAll(); // Получить всех пользователей

    boolean addFriend(Long id, Long friendId);

    boolean deleteFriend(Long id, Long friendId);

    List<User> getFriends(Long id);

    List<User> getCommonFriends(Long id, Long friendId);

    Optional<User> findById(Long userId);

    Optional<User> update(User user);

}
