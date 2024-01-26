package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.util.Collection;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDbStorage userStorage;

    @Override
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @Override
    public User create(User user) {
        return userStorage.create(user);
    }

    @Override
    public User put(User user) {
        return userStorage.put(user);
    }

    @Override
    public User get(int id) {
        return userStorage.get(id);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        userStorage.addFriend(userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        userStorage.deleteFriend(userId, friendId);
    }

    @Override
    public Collection<User> getAllFriends(int id) {
        return userStorage.getAllFriends(id);
    }

    @Override
    public Collection<User> getCommonFriends(int id, int userId) {
        return userStorage.getCommonFriends(id, userId);
    }
}
