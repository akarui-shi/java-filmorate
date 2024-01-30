package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@Component
public interface FilmStorage {
    Collection<Film> findAll();

    Film create(Film film);

    Film put(Film film);

    Film get(int id);

    void setLike(int id, int userId);

    void removeLike(int filmId, int userId);

    Collection<Film> getTopFilms(int count);

}
