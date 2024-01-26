package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.impl.FilmServiceImpl;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmServiceImpl filmService;

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Количество фильмов: {}", filmService.findAll().size());
        return filmService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody Film film) {
        log.info(film.toString());
        return filmService.create(film);
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        log.info(film.toString());
        return filmService.put(film);
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable(value = "id") int id) {
        log.info(filmService.get(id).toString());
        return filmService.get(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void setLike(@PathVariable(value = "id") int filmId,
                        @PathVariable(value = "userId") int userId) {
        log.info(filmService.get(filmId).toString());
        filmService.setLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable(value = "id") int filmId,
                           @PathVariable(value = "userId") int userId) {
        log.info(filmService.get(filmId).toString());
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        log.info(filmService.getTopFilms(count).toString());
        return filmService.getTopFilms(count);
    }
}
