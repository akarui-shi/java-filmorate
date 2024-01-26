package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.impl.MpaDbStorage;

import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class MpaServiceImpl implements MpaService {
    private final MpaDbStorage mpaStorage;

    @Override
    public Mpa get(int id) {
        return mpaStorage.get(id);
    }

    @Override
    public Collection<Mpa> findAll() {
        return mpaStorage.findAll();
    }
}
