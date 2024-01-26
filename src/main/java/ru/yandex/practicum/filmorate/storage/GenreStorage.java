package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreStorage {
    Genre get(int genreId);

    Collection<Genre> findAll();

    void reloadGenres(Film film);

    void deleteFilmGenre(Film film);

    void addFilmGenre(Film film);
}
