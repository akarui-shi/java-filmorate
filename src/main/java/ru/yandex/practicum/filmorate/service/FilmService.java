package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmService {

    Collection<Film> findAll();

    Film create(Film film);

    Film put(Film film);

    Film get(int id);

    void setLike(int id, int userId);

    void removeLike(int filmId, int userId);

    Collection<Film> getTopFilms(int count);

}
