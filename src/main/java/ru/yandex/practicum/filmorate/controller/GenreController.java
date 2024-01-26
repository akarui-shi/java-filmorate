package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.impl.GenreServiceImpl;

import java.util.Collection;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreServiceImpl genreService;

    @GetMapping()
    public Collection<Genre> findAll() {
        log.info("Количество жанров: {}", genreService.findAll().size());
        return genreService.findAll();
    }

    @GetMapping("/{id}")
    public Genre get(@PathVariable(value = "id") int id) {
        log.info(genreService.get(id).toString());
        return genreService.get(id);
    }
}
