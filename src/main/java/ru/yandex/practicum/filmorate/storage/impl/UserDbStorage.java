package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Component
@AllArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("USER_ID");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.userRowMap()).intValue());
        return user;
    }

    @Override
    public User put(User user) {
        String sqlQuery = "UPDATE USERS SET USER_EMAIL = ?, USER_LOGIN = ?, USER_NAME = ?, USER_BIRTHDAY = ? " +
                "WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public User get(int userId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE USER_ID = ?", userId);
        rs.first();
        User user = new User(
                rs.getInt("USER_ID"),
                rs.getString("USER_EMAIL"),
                rs.getString("USER_LOGIN"),
                rs.getString("USER_NAME"),
                rs.getDate("USER_BIRTHDAY").toLocalDate(),
                null
        );
        return user;
    }

    @Override
    public List<User> findAll() {
        String sqlQuery = "SELECT * FROM USERS";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new User(
                rs.getInt("USER_ID"),
                rs.getString("USER_EMAIL"),
                rs.getString("USER_LOGIN"),
                rs.getString("USER_NAME"),
                rs.getDate("USER_BIRTHDAY").toLocalDate(),
                null)
        );
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sqlQuery = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String sqlQuery = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public Collection<User> getAllFriends(int userId) {
        String sqlQuery = "SELECT USERS.* FROM FRIENDS" +
                " JOIN USERS ON FRIENDS.FRIEND_ID = USERS.USER_ID WHERE FRIENDS.USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new User(
                        rs.getInt("USER_ID"),
                        rs.getString("USER_EMAIL"),
                        rs.getString("USER_LOGIN"),
                        rs.getString("USER_NAME"),
                        rs.getDate("USER_BIRTHDAY").toLocalDate(),
                        null),
                userId
        );
    }

    @Override
    public Collection<User> getCommonFriends(int userId, int friendId) {
        String sqlQuery = " SELECT * FROM USERS WHERE USER_ID IN ((SELECT TBL1.FRIEND_ID " +
                " FROM (SELECT USER_ID, FRIEND_ID FROM FRIENDS WHERE USER_ID = ?) AS TBL1 " +
                " INNER JOIN (SELECT USER_ID, FRIEND_ID FROM FRIENDS WHERE USER_ID = ?) AS TBL2 " +
                " ON TBL1.FRIEND_ID = TBL2.FRIEND_ID)) ";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new User(
                        rs.getInt("USER_ID"),
                        rs.getString("USER_EMAIL"),
                        rs.getString("USER_LOGIN"),
                        rs.getString("USER_NAME"),
                        rs.getDate("USER_BIRTHDAY").toLocalDate(),
                        null),
                userId, friendId);
    }

    public boolean isRegistered(int userId) {
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        SqlRowSet userRow = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        return userRow.next();
    }
}
