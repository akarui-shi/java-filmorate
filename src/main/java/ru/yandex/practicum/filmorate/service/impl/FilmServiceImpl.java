package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;

import java.util.Collection;

@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmDbStorage filmStorage;

    @Override
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @Override
    public Film create(Film film) {
        return filmStorage.create(film);
    }

    @Override
    public Film put(Film film) {
        return filmStorage.put(film);
    }

    @Override
    public Film get(int id) {
        return filmStorage.get(id);
    }

    @Override
    public void setLike(int filmId, int userId) {
        filmStorage.setLike(filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        filmStorage.removeLike(filmId, userId);
    }

    @Override
    public Collection<Film> getTopFilms(int count) {
        return filmStorage.getTopFilms(count);
    }
}

