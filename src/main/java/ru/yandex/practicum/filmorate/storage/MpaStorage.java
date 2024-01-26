package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Component
public interface MpaStorage {
    Mpa get(int id);

    Collection<Mpa> findAll();
}
