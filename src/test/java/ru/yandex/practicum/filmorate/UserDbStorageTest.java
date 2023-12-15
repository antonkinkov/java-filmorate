package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
public class UserDbStorageTest {

    private final UserStorage userStorage;

    @Autowired
    public UserDbStorageTest(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Test
    @Order(10)
    void createAndGetAllUserTest() {
        userStorage.create(User.builder()
                .name("Nick Name")
                .login("dolore")
                .email("mail@mail.ru")
                .birthday(LocalDate.parse("1946-08-20"))
                .build());
        assertEquals(1, userStorage.getAll().size());
    }

    @Test
    @Order(20)
    void updateTest() {
        User user = userStorage.create(User.builder()
                .name("Nick Name")
                .login("dolore")
                .email("mail@mail.ru")
                .birthday(LocalDate.parse("1946-08-20"))
                .build());

        user.setName("Nick Name updated");
        userStorage.update(user);

        Optional<User> userOptional = userStorage.findById(user.getId());

        assertThat(userOptional).isPresent()
                .hasValueSatisfying(u -> assertThat(u)
                        .hasFieldOrPropertyWithValue("name", "Nick Name updated"));
    }


    @Test
    @Order(20)
    void getByUdTest() {
        User user = userStorage.create(User.builder()
                .name("Nick Name")
                .login("dolore")
                .email("mail@mail.ru")
                .birthday(LocalDate.parse("1946-08-20"))
                .build());

        Optional<User> userOptional = userStorage.findById(user.getId());

        assertThat(userOptional).isPresent()
                .hasValueSatisfying(u -> assertThat(u)
                        .hasFieldOrPropertyWithValue("login", "dolore"));
    }

    @Test
    void addFriendTest() {
        User firstUser = userStorage.create(User.builder()
                .name("First Nick Name")
                .login("fLogin")
                .email("fMail@mail.ru")
                .birthday(LocalDate.parse("1946-08-20"))
                .build());

        User secondUser = userStorage.create(User.builder()
                .name("Second Nick Name")
                .login("sLogin")
                .email("sMail@mail.ru")
                .birthday(LocalDate.parse("1946-08-20"))
                .build());

        userStorage.addFriend(firstUser.getId(), secondUser.getId());

        List<User> firstUserFriends = userStorage.getFriends(firstUser.getId());

        assertEquals(1, firstUserFriends.size());

    }

    @Test
    void deleteFriendTest() {
        User firstUser = userStorage.create(User.builder()
                .name("First Nick Name")
                .login("fLogin")
                .email("fMail@mail.ru")
                .birthday(LocalDate.parse("1946-08-20"))
                .build());

        User secondUser = userStorage.create(User.builder()
                .name("Second Nick Name")
                .login("sLogin")
                .email("sMail@mail.ru")
                .birthday(LocalDate.parse("1946-08-20"))
                .build());

        userStorage.addFriend(firstUser.getId(), secondUser.getId());

        boolean isDeleted = userStorage.deleteFriend(firstUser.getId(), secondUser.getId());

        assertTrue(isDeleted);

    }
}
