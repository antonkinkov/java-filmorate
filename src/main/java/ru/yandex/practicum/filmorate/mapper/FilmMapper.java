package ru.yandex.practicum.filmorate.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmMapper implements ResultSetExtractor<List<Film>> {

    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Film> films = new HashMap<>();

        while (rs.next()) {
            long id = rs.getLong(MapperConstants.ID.getLowerCaseName());

            films.putIfAbsent(id, Film.builder()
                    .id(rs.getLong(MapperConstants.ID.getLowerCaseName()))
                    .name(rs.getString(MapperConstants.NAME.getLowerCaseName()))
                    .description(rs.getString(MapperConstants.DESCRIPTION.getLowerCaseName()))
                    .releaseDate(rs.getDate(MapperConstants.RELEASE_DATE.getLowerCaseName()).toLocalDate())
                    .duration(rs.getInt(MapperConstants.DURATION.getLowerCaseName()))
                    .mpa(Mpa.builder()
                            .id(rs.getLong(MapperConstants.MPA_ID.getLowerCaseName()))
                            .name(rs.getString(MapperConstants.MPA_NAME.getLowerCaseName()))
                            .build())
                    .genres(new ArrayList<>())
                    .build());

            long genreId = rs.getLong(MapperConstants.GENRE_ID.getLowerCaseName());

            if (genreId != 0) {
                films.get(id).getGenres()
                        .add(Genre.builder()
                                .id(genreId)
                                .name(rs.getString(MapperConstants.GENRE_NAME.getLowerCaseName()))
                                .build()
                        );
            }
        }
        return new ArrayList<>(films.values());
    }
}
