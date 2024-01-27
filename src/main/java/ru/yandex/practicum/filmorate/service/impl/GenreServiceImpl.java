package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
        if (genreStorage.isRegistered(id)) {
            log.info("Жанр с id = {} возвращён.", id);
            return genreStorage.get(id);
        } else {
            throw new NotFoundException("Жанр с id = " + id + " не найден в списке.");
        }
    }

    @Override
    public Collection<Genre> findAll() {
        Collection<Genre> genres = genreStorage.findAll();
        log.info("Текущее количество жанров: {}. Список возвращён.", genres.size());
        return genres;
    }
}
