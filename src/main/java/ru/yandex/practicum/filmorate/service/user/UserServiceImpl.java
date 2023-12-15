package ru.yandex.practicum.filmorate.service.user;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public User create(@NonNull User user) {

        if (Objects.isNull(user.getName()) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        return userStorage.create(user);
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public boolean addFriend(Long id, Long friendId) {
        validateUserId(id);
        validateUserId(friendId);

        return userStorage.addFriend(id, friendId);
    }

    @Override
    public boolean deleteFriend(Long id, Long friendId) {
        validateUserId(id);
        validateUserId(friendId);
        return userStorage.deleteFriend(id, friendId);
    }

    @Override
    public List<User> getFriends(Long id) {
        validateUserId(id);
        return userStorage.getFriends(id);
    }

    @Override
    public List<User> getCommonFriends(Long id, Long friendId) {
        User sourceUser = validateUserId(id);
        validateUserId(friendId);


        if (sourceUser.getFriends().isEmpty()) {
            return new ArrayList<>();
        }

        return userStorage.getCommonFriends(id, friendId);
    }

    @Override
    public User findById(Long userId) {
        return validateUserId(userId);
    }

    @Override
    public User update(User user) {
        validateUserId(user.getId());

        return userStorage.update(user).orElseThrow(() -> {
            log.info("Ошибка при обновлении пользователя");
            return new EntityNotFoundException("Ошибка при обновлении пользователя с id= " + user.getId());
        });
    }

    private User validateUserId(Long id) {
        return userStorage.findById(id)
                .orElseThrow(() -> {
                    log.error("Ошибка при валидации пользователя.");
                    return new EntityNotFoundException("Пользователь с id=" + id + "не найден.");
                });
    }

}
