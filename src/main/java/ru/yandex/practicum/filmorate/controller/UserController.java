package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    @Autowired
    private final UserServiceImpl userService;

    @GetMapping
    public Collection<User> findAll() {
        log.info("Количество пользователей: {}", userService.findAll().size());
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info(String.valueOf(user));
        return userService.create(user);
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        log.info(user.toString());
        return userService.put(user);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        log.info(userService.get(id).toString());
        return userService.get(id);
    }

    @PutMapping("{id}/friends/{friendId}")
    public User addNewFriend(@PathVariable(value = "id") int userId,
                             @PathVariable(value = "friendId") int friendId) {
        log.info(userService.get(userId).toString());
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable(value = "id") int userId,
                             @PathVariable(value = "friendId") int friendId) {
        log.info(userService.get(userId).toString());
        return userService.deleteFriend(userId, friendId);
    }

    @GetMapping("{id}/friends")
    public Collection<User> getAllFriends(@PathVariable(value = "id") int id) {
        log.info(userService.getAllFriends(id).toString());
        return userService.getAllFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable(value = "id") int id,
                                       @PathVariable(value = "otherId") int otherId) {
        log.info(userService.getCommonFriends(id, otherId).toString());
        return userService.getCommonFriends(id, otherId);
    }

}
