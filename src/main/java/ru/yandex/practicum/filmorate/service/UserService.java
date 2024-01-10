package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserService {
    Collection<User> findAll();

    User create(User user);

    User put(User user);

    User get(int id);

    User addFriend(int userId, int friendId);

    User deleteFriend(int userId, int friendId);

    Collection<User> getAllFriends(int id);

    Collection<User> getCommonFriends(int id, int userId);

}
