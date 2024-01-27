package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDbStorage userStorage;

    @Override
    public Collection<User> findAll() {
        Collection<User> users = userStorage.findAll();
        log.info("Текущее количество пользователей: {}. Список возвращён.", users.size());
        return users;
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.info("Добавлен новый пользователь с id = {}", user.getId());
        return userStorage.create(user);
    }

    @Override
    public User put(User user) {
        if (get(user.getId()) != null) {
            log.info("Пользователь с id = {} обновлён.", user.getId());
            return userStorage.put(user);
        } else {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден в списке.");
        }
    }

    @Override
    public User get(int id) {
        if (userStorage.isRegistered(id)) {
            log.info("Пользователь с id = {} возвращён.", id);
            return userStorage.get(id);
        } else {
            throw new NotFoundException("Пользователь с id = " + id + " не найден в списке.");
        }
    }

    @Override
    public void addFriend(int userId, int friendId) {
        if (userStorage.isRegistered(userId) && userStorage.isRegistered(friendId)) {
            log.info("Пользователи с id = {} добавил в друзья пользователя с id = {}.", userId, friendId);
            userStorage.addFriend(userId, friendId);
        } else {
            throw new NotFoundException("Пользователь не зарегистрирован.");
        }
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        if (userStorage.isRegistered(userId) && userStorage.isRegistered(friendId)) {
            log.info("Пользователи с id = {} удалил из друзей пользователя с id = {}.", userId, friendId);
            userStorage.deleteFriend(userId, friendId);
        } else {
            throw new NotFoundException("Пользователь не зарегистрирован.");
        }
    }

    @Override
    public Collection<User> getAllFriends(int id) {
        if (userStorage.isRegistered(id)) {
            log.info("Список друзей пользователя с id = {} возвращён.", id);
            return userStorage.getAllFriends(id);
        } else {
            throw new NotFoundException("Пользователь с id = " + id + " не найден в списке.");
        }
    }

    @Override
    public Collection<User> getCommonFriends(int id, int userId) {
        if (userStorage.isRegistered(id) && userStorage.isRegistered(userId)) {
            log.info("Список общих друзей пользователей с id = {} и с id = {} возвращён.", id, userId);
            return userStorage.getCommonFriends(id, userId);
        } else {
            throw new NotFoundException("Пользователь не зарегистрирован.");
        }
    }
}
