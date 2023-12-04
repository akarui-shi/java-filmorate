package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Количество фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))){
            throw new ValidationException("Ошибка валидации данных фильма.");
        }

        id++;
        film.setId(id);
        films.put(id, film);
        log.info(film.toString());

        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Ошибка валидации данных фильма.");
        }
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("[eyq");
        }
        log.info(film.toString());
        films.put(film.getId(), film);
        return film;
    }
}
