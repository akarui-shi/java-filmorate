package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserServiceImpl(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @Override
    public Collection<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    @Override
    public User create(User user) {
        return inMemoryUserStorage.create(user);
    }

    @Override
    public User put(User user) {
        return inMemoryUserStorage.put(user);
    }

    @Override
    public User get(int id) {
        return inMemoryUserStorage.get(id);
    }

    @Override
    public User addFriend(int userId, int friendId) {
        User user = get(userId);
        User friend = get(friendId);
        if (!user.getFriends().contains(friendId)) {
            user.getFriends().add(friendId);
            friend.getFriends().add(userId);
        }
        return user;
    }

    @Override
    public User deleteFriend(int userId, int friendId) {
        User user = get(userId);
        User friend = get(friendId);
        if (user.getFriends().contains(friendId)) {
            user.getFriends().remove(friendId);
            friend.getFriends().remove(userId);
        }
        return user;
    }

    @Override
    public List<User> getAllFriends(int id) {
        User user = get(id);
        return user.getFriends()
                .stream()
                .map(this::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(int id, int userId) {
        Collection<User> userFriends = getAllFriends(id);
        Collection<User> otherUserFriends = getAllFriends(userId);
        return userFriends.stream()
                .filter(otherUserFriends::contains)
                .collect(Collectors.toList());
    }
}
