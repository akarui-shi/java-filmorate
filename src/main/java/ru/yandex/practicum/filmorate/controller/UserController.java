package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.impl.UserServiceImpl;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    @Autowired
    private final UserServiceImpl userService;

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        return userService.put(user);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        return userService.get(id);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addNewFriend(@PathVariable(value = "id") int userId,
                             @PathVariable(value = "friendId") int friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable(value = "id") int userId,
                             @PathVariable(value = "friendId") int friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("{id}/friends")
    public Collection<User> getAllFriends(@PathVariable(value = "id") int id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable(value = "id") int id,
                                       @PathVariable(value = "otherId") int otherId) {
        return userService.getCommonFriends(id, otherId);
    }

}
