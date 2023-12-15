package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    User create(User user); // Добавить пользователя

    List<User> getAll(); // Получить всех пользователей

    boolean addFriend(Long id, Long friendId);

    boolean deleteFriend(Long id, Long friendId);

    List<User> getFriends(Long id);

    List<User> getCommonFriends(Long id, Long friendId);

    User findById(Long userId);

    User update(User user);
}


