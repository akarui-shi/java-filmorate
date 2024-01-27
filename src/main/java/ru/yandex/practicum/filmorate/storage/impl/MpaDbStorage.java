package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@AllArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa get(int mpaId) {
        String sqlQuery = "SELECT * FROM MPA WHERE MPA_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, mpaId);
        rs.next();
        return mpaRowMap(rs);
    }

    @Override
    public Collection<Mpa> findAll() {
        List<Mpa> mpaList = new ArrayList<>();
        String sqlQuery = "SELECT * FROM MPA";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery);
        while (rs.next()) {
            mpaList.add(mpaRowMap(rs));
        }
        return mpaList;
    }

    private Mpa mpaRowMap(SqlRowSet rs) {
        return new Mpa(
                rs.getInt("MPA_ID"),
                rs.getString("MPA_NAME")
        );
    }

    public boolean isRegistered(int mpaId) {
        String sqlQuery = "SELECT * FROM MPA WHERE MPA_ID = ?";
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(sqlQuery, mpaId);
        return mpaRow.next();
    }
}
