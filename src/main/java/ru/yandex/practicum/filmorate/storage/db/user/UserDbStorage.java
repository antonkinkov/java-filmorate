package ru.yandex.practicum.filmorate.storage.db.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.MapperConstants;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.*;

import static java.util.Objects.isNull;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        String sqlQuery = "INSERT INTO users (email, login, name, birthday)" +
                "VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(sqlQuery, new String[]{MapperConstants.ID.getLowerCaseName()});

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getName());

            final LocalDate birthday = user.getBirthday();
            if (isNull(birthday)) {
                statement.setNull(4, Types.DATE);
            } else {
                statement.setDate(4, Date.valueOf(birthday));
            }
            return statement;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return user;
    }

    @Override
    public List<User> getAll() {
        String query = "SELECT id, email, login, name, birthday FROM users";

        return jdbcTemplate.query(query, new UserMapper());
    }

    @Override
    public boolean addFriend(Long id, Long friendId) {
        String query = "INSERT INTO user_friends (user_id, friend_id, status) VALUES (?, ?, ?)";
        return jdbcTemplate.update(query, id, friendId, true) != 0;
    }

    @Override
    public boolean deleteFriend(Long id, Long friendId) {
        String query = "DELETE FROM user_friends " +
                "WHERE (user_id = ? AND friend_id = ?) " +
                "OR (user_id = ? AND friend_id = ?)";

        return jdbcTemplate.update(query, id, friendId, friendId, id) != 0;
    }

    @Override
    public List<User> getCommonFriends(Long id, Long friendId) {
        String query = "SELECT u.* " +
                "FROM user_friends AS fs " +
                "JOIN users AS u ON fs.friend_id = u.id " +
                "WHERE fs.user_id = ? AND fs.friend_id IN " +
                "(SELECT friend_id FROM user_friends WHERE user_id = ?)";

        return jdbcTemplate.query(query, new UserMapper(), id, friendId);
    }

    @Override
    public List<User> getFriends(Long id) {
        String query = "SELECT " +
                "f.friend_id AS id, " +
                "u.login, " +
                "u.name, " +
                "u.email, " +
                "u.birthday " +
                "FROM user_friends AS f " +
                "LEFT JOIN users AS u ON u.id = f.friend_id " +
                "WHERE f.user_id = ?";

        return jdbcTemplate.query(query, new UserMapper(), id);
    }

    @Override
    public Optional<User> findById(Long userId) {

        String query = "SELECT * FROM users WHERE id = ?";
        List<User> users = jdbcTemplate.query(query, new UserMapper(), userId);

        if (!users.isEmpty()) {
            User user = users.get(0);
            user.setFriends(getFriendsByUserId(user.getId()));
            return Optional.of(user);
        }

        return Optional.empty();
    }

    private Set<Long> getFriendsByUserId(long id) {
        String query = "SELECT friend_id FROM user_friends WHERE user_id = ?";

        return new HashSet<>(jdbcTemplate.query(query, (rs, num) -> rs.getLong("friend_id"), id));
    }

    @Override
    public Optional<User> update(User user) {
        String query = "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? " +
                "WHERE id = ?";

        return jdbcTemplate.update(query, user.getName(),
                user.getLogin(), user.getEmail(), user.getBirthday(), user.getId()) == 0 ? Optional.empty() : Optional.of(user);
    }
}
