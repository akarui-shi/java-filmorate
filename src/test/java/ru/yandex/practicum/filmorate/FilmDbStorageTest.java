package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    final FilmDbStorage filmStorage;
    final GenreDbStorage genreStorage;
    final UserDbStorage userDbStorage;

    @BeforeEach
    void createFilmData() {
        if (filmStorage.findAll().size() != 2) {
            Set<Genre> genres = new HashSet<>();
            genres.add(new Genre(2, genreStorage.get(2).getName()));

            Film film1 = new Film(null,"Film1", "Description1", LocalDate.parse("1970-01-01"),
                    140, new Mpa(1, "G"), 0);
            filmStorage.create(film1);

            Film film2 = new Film(null, "Film2", "Description2", LocalDate.parse("1980-01-01"),
                    90, new Mpa(2, "PG"), 0);
            filmStorage.create(film2);
        }
        if (userDbStorage.findAll().size() != 2) {
            User firstUser = new User(1, "email@yandex.ru", "Login1", "Name1", LocalDate.parse("1970-01-01"), null);
            userDbStorage.create(firstUser);
            User secontUser = new User(1, "email@gmail.com", "Login2", "Name2", LocalDate.parse("1980-01-01"), null);
            userDbStorage.create(secontUser);
        }
    }

    @Test
    @DisplayName("Проверка метода update для Film")
    void testUpdateFilm() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(2, genreStorage.get(2).getName()));
        Film updateFilm = new Film(1, "Film1", "updateDescription", LocalDate.parse("1990-01-01"), 140, new Mpa(1, "G"), 0);
        filmStorage.put(updateFilm);
        Film afterUpdate = filmStorage.put(updateFilm);
        Assertions.assertEquals(afterUpdate.getDescription(), "updateDescription");
    }

    @Test
    @DisplayName("Проверка метода findById для Film")
    void testFindFilmById() {
        Film film = filmStorage.get(1);
        Assertions.assertEquals(film.getId(), 1);
    }

    @Test
    @DisplayName("Проверка метода findAll() для Film")
    void testFindAll() {
        List<Film> current = filmStorage.findAll();
        Assertions.assertEquals(2, current.size(), "Количество фильмов не совпадает");
    }
}
