package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.impl.MpaServiceImpl;

import java.util.Collection;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MpaServiceImpl mpaService;

    @GetMapping("/{id}")
    public Mpa get(@PathVariable(value = "id") int id) {
        log.info(mpaService.get(id).toString());
        return mpaService.get(id);
    }

    @GetMapping
    public Collection<Mpa> findAll() {
        log.info("Количество MPA: {}", mpaService.findAll().size());
        return mpaService.findAll();
    }
}
