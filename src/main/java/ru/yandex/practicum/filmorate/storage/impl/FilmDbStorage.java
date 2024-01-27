package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@AllArgsConstructor
@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> findAll() {
        String sqlQuery = "SELECT * FROM FILMS";
        List<Film> films = new ArrayList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery);
        while (rs.next()) {
            films.add(filmRowMap(rs));
        }
        return films;
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.filmToMap()).intValue());
        return get(film.getId());
    }

    @Override
    public Film put(Film film) {
        String sqlQuery = "UPDATE FILMS SET FILM_NAME = ?, FILM_DESCRIPTION = ?, FILM_RELEASE_DATE = ?, " +
                "FILM_DURATION = ?, MPA_ID = ? WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    @Override
    public Film get(int filmId) {
        String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        rs.next();
        return filmRowMap(rs);
    }

    @Override
    public Collection<Film> getTopFilms(int limit) {
        List<Film> films = new ArrayList<>();
        String sqlQuery = "SELECT F.*, COUNT(L.ID) FROM FILMS AS F " +
                "LEFT JOIN LIKES AS L ON F.FILM_ID = L.FILM_ID " +
                "GROUP BY F.FILM_ID ORDER BY COUNT(L.ID) DESC LIMIT ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, limit);
        while (rs.next()) {
            films.add(filmRowMap(rs));
        }
        return films;
    }

    private Film filmRowMap(SqlRowSet rs) {
        Film film = new Film(
                rs.getInt("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("FILM_DESCRIPTION"),
                rs.getDate("FILM_RELEASE_DATE").toLocalDate(),
                rs.getInt("FILM_DURATION"),
                getMpa(rs.getInt("MPA_ID")),
                getFilmLikes(rs.getInt("FILM_ID")));
        film.setGenres(getFilmGenres(film.getId()));
        return film;
    }

    public Mpa getMpa(int mpaId) {
        String sqlQuery = "SELECT * FROM MPA WHERE MPA_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, mpaId);
        if (rs.next()) {
            return mpaRowMap(rs);
        } else {
            throw new NotFoundException("MPA с id = " + mpaId + " не найден в списке.");
        }
    }

    private Mpa mpaRowMap(SqlRowSet rs) {
        return new Mpa(
                rs.getInt("MPA_ID"),
                rs.getString("MPA_NAME")
        );
    }

    private Genre genreRowMap(SqlRowSet rs) {
        return new Genre(
                rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME")
        );
    }

    private int getFilmLikes(int filmId) {
        String sqlQuery = "SELECT COUNT(FILM_ID) AS AMOUNT FROM LIKES WHERE FILM_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        if (rs.next()) {
            return rs.getInt("AMOUNT");
        } else {
            return 0;
        }
    }

    public Set<Genre> getFilmGenres(int filmId) {
        Set<Genre> filmGenres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
        String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID IN " +
                "(SELECT GENRE_ID FROM FILM_GENRES WHERE FILM_ID = ?)";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        while (rs.next()) {
            filmGenres.add(genreRowMap(rs));
        }
        return filmGenres;
    }

    public void fillFilmGenres(Film film) {
        List<Genre> genres = List.copyOf(film.getGenres());
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

    public void deleteFilmGenres(int filmId) {
        String sqlQuery = "DELETE FROM FILM_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public void setLike(int filmId, int userId) {
        String sqlQuery = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        String sqlQuery = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public boolean isRegistered(int filmId) {
        String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        SqlRowSet filmRow = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        return filmRow.next();
    }
}