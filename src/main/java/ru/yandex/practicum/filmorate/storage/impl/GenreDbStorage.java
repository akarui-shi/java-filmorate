package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre get(int genreId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES WHERE GENRE_ID = ?", genreId);
        rs.next();
        return new Genre(
                rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME")
        );
    }

    @Override
    public List<Genre> findAll() {
        List<Genre> genres = new ArrayList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES");
        while (rs.next()) {
            Genre genre = new Genre(
                    rs.getInt("GENRE_ID"),
                    rs.getString("GENRE_NAME")
            );
            genres.add(genre);
        }
        return genres;
    }

    @Override
    public void addFilmGenre(Film film) {
        List<Genre> genres = List.copyOf(film.getGenres());
        if (!genres.isEmpty()) {
            String sqlQuery = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
            jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, film.getId());
                    ps.setInt(2, genres.get(i).getId());
                }

                @Override
                public int getBatchSize() {
                    return genres.size();
                }
            });
        }
    }

    public boolean isRegistered(int genreId) {
        String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet(sqlQuery, genreId);
        return genreRow.next();
    }
}
