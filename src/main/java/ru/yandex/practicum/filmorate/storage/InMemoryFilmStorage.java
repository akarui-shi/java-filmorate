package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        id++;
        film.setId(id);
        films.put(id, film);
        return film;
    }

    @Override
    public Film put(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм " + film.getName() + " не найден в списке.");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film get(int id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм c id = " + id + " не найден в списке.");
        }
        return films.get(id);
    }
}
