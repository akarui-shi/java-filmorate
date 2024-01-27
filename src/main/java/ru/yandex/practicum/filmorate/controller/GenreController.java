package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.impl.GenreServiceImpl;

import java.util.Collection;

@AllArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreServiceImpl genreService;

    @GetMapping()
    public Collection<Genre> findAll() {
        return genreService.findAll();
    }

    @GetMapping("/{id}")
    public Genre get(@PathVariable(value = "id") int id) {
        return genreService.get(id);
    }
}
