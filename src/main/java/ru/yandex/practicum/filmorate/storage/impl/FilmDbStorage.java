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
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@AllArgsConstructor
@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

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
        genreStorage.addFilmGenre(film);
        return get(film.getId());
    }

    @Override
    public Film put(Film film) {
        if (getAllFilms(film.getId()).next()) {
            String sqlQuery = "UPDATE FILMS SET FILM_NAME = ?, FILM_DESCRIPTION = ?, FILM_RELEASE_DATE = ?, " +
                    "FILM_DURATION = ?, MPA_ID = ? WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
            mpaStorage.get(film.getMpa().getId());
            deleteFilmGenres(film.getId());
            fillFilmGenres(film);
            film.setGenres(getFilmGenres(film.getId()));
            return film;
        } else {
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден в списке.");
        }
    }

    @Override
    public Film get(int filmId) {
        String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        if (rs.next()) {
            return filmRowMap(rs);
        } else {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден в списке.");
        }
    }

    @Override
    public Collection<Film> getTopFilms(int limit) {
        List<Film> films = new ArrayList<>();
        String sqlQuery = "SELECT F.*, COUNT(L.ID) FROM FILMS AS F " +
                "LEFT JOIN LIKES AS L ON F.FILM_ID = L.FILM_ID " +
                "GROUP BY F.FILM_ID ORDER BY COUNT(L.ID) DESC LIMIT ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, limit);
        System.out.println(rs);
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
                mpaStorage.get(rs.getInt("MPA_ID")),
                getFilmLikes(rs.getInt("FILM_ID")));
        film.setGenres(getFilmGenres(film.getId()));
        return film;
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

    private Set<Genre> getFilmGenres(int filmId) {
        Set<Genre> filmGenres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
        String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID IN " +
                "(SELECT GENRE_ID FROM FILM_GENRES WHERE FILM_ID = ?)";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        while (rs.next()) {
            filmGenres.add(genreRowMap(rs));
        }
        return filmGenres;
    }

    private SqlRowSet getAllFilms(int filmId) {
        String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        return jdbcTemplate.queryForRowSet(sqlQuery, filmId);
    }

    private void fillFilmGenres(Film film) {
        isGenreRegistered(film);
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

    private void deleteFilmGenres(int filmId) {
        String sqlQuery = "DELETE FROM FILM_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    private void isGenreRegistered(Film film) {
        String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        for (Genre genre : film.getGenres()) {
            if (!jdbcTemplate.queryForRowSet(sqlQuery, genre.getId()).next()) {
                throw new NotFoundException("Жанр с id = " + genre.getId() + " не найден в списке.");
            }
        }
    }

    @Override
    public void setLike(int filmId, int userId) {
        isFilmRegistered(filmId);
        isUserRegistered(userId);
        String sqlQuery = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        isFilmRegistered(filmId);
        isUserRegistered(userId);
        String sqlQuery = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    private void isFilmRegistered(int filmId) {
        String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        SqlRowSet filmRow = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        if (!filmRow.next()) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден в списке.");
        }
    }

    private void isUserRegistered(int userId) {
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        SqlRowSet userRow = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        if (!userRow.next()) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден в списке.");
        }
    }
}