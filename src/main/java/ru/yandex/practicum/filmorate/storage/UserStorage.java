package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Component
public interface UserStorage {

    Collection<User> findAll();

    User create(User user);

    User put(User user);

    User get(int id);
    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    Collection<User> getAllFriends(int id);

    Collection<User> getCommonFriends(int id, int userId);
}
