package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class UserMapper implements RowMapper<User> {


    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        Date birthday = rs.getDate(MapperConstants.BIRTHDAY.getLowerCaseName());
        return User.builder()
                .id(rs.getLong(MapperConstants.ID.getLowerCaseName()))
                .email(rs.getString(MapperConstants.EMAIL.getLowerCaseName()))
                .login(rs.getString(MapperConstants.LOGIN.getLowerCaseName()))
                .name(rs.getString(MapperConstants.NAME.getLowerCaseName()))
                .birthday(Objects.nonNull(birthday) ? birthday.toLocalDate() : null)
                .build();
    }
}
