package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        id++;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User put(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (!users.containsKey(user.getId()))
            throw new NotFoundException("Пользователь " + user.getEmail() + "не найден в списке.");
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User get(int id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь c id = " + id + "не найден в списке.");
        }
        return users.get(id);
    }
}
