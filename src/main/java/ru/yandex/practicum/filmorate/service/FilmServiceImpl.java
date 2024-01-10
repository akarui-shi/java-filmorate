package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class FilmServiceImpl implements FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmServiceImpl(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    @Override
    public Collection<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }

    @Override
    public Film create(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Ошибка валидации данных фильма.");
        }
        return inMemoryFilmStorage.create(film);
    }

    @Override
    public Film put(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Ошибка валидации данных фильма.");
        }
        return inMemoryFilmStorage.put(film);
    }

    @Override
    public Film get(int id) {
        return inMemoryFilmStorage.get(id);
    }

    @Override
    public Film setLike(int filmId, int userId) {
        if (userId <= 0) throw new NotFoundException("Пользователь c id = " + userId + "не найден в списке.");
        Film film = get(filmId);
        film.getLikes().add(userId);
        return film;
    }

    @Override
    public Film removeLike(int filmId, int userId) {
        if (userId <= 0) throw new NotFoundException("Пользователь c id = " + userId + "не найден в списке.");
        Film film = get(filmId);
        film.getLikes().remove(userId);
        return film;
    }

    @Override
    public Collection<Film> getTopFilms(int count) {
        return findAll()
                .stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}

