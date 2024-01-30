package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmDbStorage filmStorage;
    private final GenreDbStorage genreStorage;
    private final MpaDbStorage mpaStorage;
    private final UserDbStorage userStorage;

    @Override
    public Collection<Film> findAll() {
        Collection<Film> films = filmStorage.findAll();
        log.info("Текущее количество фильмов: {}. Список возвращён.", films.size());
        return films;
    }

    @Override
    public Film create(Film film) {
        filmStorage.create(film);
        genreStorage.addFilmGenre(film);
        log.info("Добавлен новый фильм с id = {}", film.getId());
        return film;
    }

    @Override
    public Film put(Film film) {
        if (get(film.getId()) != null) {
            filmStorage.put(film);
            mpaStorage.get(film.getMpa().getId());
            filmStorage.deleteFilmGenres(film.getId());
            if (isGenresRegistered(film)) {
                filmStorage.fillFilmGenres(film);
            }
            film.setGenres(filmStorage.getFilmGenres(film.getId()));
            log.info("Фильм с id = {} обновлён.", film.getId());
            return film;
        } else {
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден в списке.");
        }
    }

    @Override
    public Film get(int id) {
        if (filmStorage.isRegistered(id)) {
            log.info("Фильм с id = {} возвращён.", id);
            return filmStorage.get(id);
        } else {
            throw new NotFoundException("Фильм с id = " + id + " не найден в списке.");
        }
    }

    @Override
    public void setLike(int filmId, int userId) {
        if (filmStorage.isRegistered(filmId) && userStorage.isRegistered(userId)) {
            log.info("Пользователь с id = {} лайкнул фильм с id = {}.", userId, filmId);
            filmStorage.setLike(filmId, userId);
        } else if (!filmStorage.isRegistered(filmId)) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден в списке.");
        } else if (!userStorage.isRegistered(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден в списке.");
        }
    }

    @Override
    public void removeLike(int filmId, int userId) {
        if (filmStorage.isRegistered(filmId) && userStorage.isRegistered(userId)) {
            log.info("Пользователь с id = {} удалил лайк к фильму с id = {}.", userId, filmId);
            filmStorage.removeLike(filmId, userId);
        } else if (!filmStorage.isRegistered(filmId)) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден в списке.");
        } else if (!userStorage.isRegistered(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден в списке.");
        }
    }

    @Override
    public Collection<Film> getTopFilms(int count) {
        log.info("Список популярных фильмов возвращён.");
        return filmStorage.getTopFilms(count);
    }

    private boolean isGenresRegistered(Film film) {
        for (Genre genre : film.getGenres()) {
            if (!genreStorage.isRegistered(genre.getId())) {
                throw new NotFoundException("Жанр с id = " + genre.getId() + " не найден в списке.");
            }
        }
        return true;
    }
}

