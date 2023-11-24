package ru.yandex.practicum.filmorate.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public User addUser(@NonNull User user) {

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
        User sourceUser = validateUserId(id);
        User targetUser = validateUserId(friendId);

        sourceUser.getFriends().add(targetUser.getId());
        targetUser.getFriends().add(sourceUser.getId());
        return true;
    }

    @Override
    public boolean deleteFriend(Long id, Long friendId) {
        User sourceUser = validateUserId(id);
        User targetUser = validateUserId(friendId);

        sourceUser.getFriends().remove(targetUser.getId());
        targetUser.getFriends().remove(sourceUser.getId());
        return true;
    }

    @Override
    public List<User> getFriends(Long id) {
        User currentUser = validateUserId(id);

        Set<Long> friendId = currentUser.getFriends();

        return userStorage.getAll().stream()
                .filter(user -> friendId.contains(user.getId()))
                .collect(Collectors.toList());

    }

    @Override
    public List<User> getCommonFriends(Long id, Long friendId) {
        User sourceUser = validateUserId(id);
        User targetUser = validateUserId(friendId);


        if (sourceUser.getFriends().isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> userId = sourceUser.getFriends()
                .stream()
                .filter(identity -> targetUser.getFriends().contains(identity))
                .collect(Collectors.toList());

        return userStorage.getAll().stream()
                .filter(user -> userId.contains(user.getId()))
                .collect(Collectors.toList());

    }

    @Override
    public User findById(Long userId) {
        return validateUserId(userId);
    }

    @Override
    public User update(User user) {
        User curUser = validateUserId(user.getId());

        curUser.setName(user.getName());
        curUser.setLogin(user.getLogin());
        curUser.setBirthday(user.getBirthday());
        curUser.setEmail(user.getEmail());

        log.debug("Данные пользователя успешно обновлены.");
        return curUser;

    }

    private User validateUserId(Long id) {
        return userStorage.findById(id)
                .orElseThrow(() -> {
                    log.error("Ошибка при валидации пользователя.");
                    return new EntityNotFoundException("Пользователь с id=" + id + "не найден.");
                });
    }

}
