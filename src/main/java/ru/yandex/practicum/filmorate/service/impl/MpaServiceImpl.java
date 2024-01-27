package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
        if (mpaStorage.isRegistered(id)) {
            log.info("Mpa с id = {} возвращён.", id);
            return mpaStorage.get(id);
        } else {
            throw new NotFoundException("Mpa с id = " + id + " не найден в списке.");
        }
    }

    @Override
    public Collection<Mpa> findAll() {
        Collection<Mpa> mpa =  mpaStorage.findAll();
        log.info("Текущее количество mpa: {}. Список возвращён.", mpa.size());
        return mpa;
    }
}
