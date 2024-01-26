package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.impl.GenreDbStorage;

import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreDbStorage genreStorage;

    @Override
    public Genre get(int id) {
        return genreStorage.get(id);
    }

    @Override
    public Collection<Genre> findAll() {
        return genreStorage.findAll();
    }
}
