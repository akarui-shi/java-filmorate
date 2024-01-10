package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmServiceImpl;
import ru.yandex.practicum.filmorate.service.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmorateApplicationTests {
	private final FilmController filmController = new FilmController(new FilmServiceImpl(new InMemoryFilmStorage()));
	private final UserController userController = new UserController(new UserServiceImpl(new InMemoryUserStorage()));

	@Test
	public void testCreateFilmReleaseDate() {
		Film film = Film.builder()
				.releaseDate(LocalDate.of(2021, 9, 1))
				.build();
		Assertions.assertDoesNotThrow(() -> filmController.create(film));

		film.setReleaseDate(LocalDate.of(1895, 12, 27));
		Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
	}

	@Test
	public void testPutFilm() {
		Film film = Film.builder()
				.id(100)
				.name("Film name")
				.description("Film description")
				.releaseDate(LocalDate.of(2021, 9, 1))
				.duration(100)
				.build();

		Assertions.assertThrows(NotFoundException.class, () -> filmController.put(film));

		filmController.create(film);
		Assertions.assertDoesNotThrow(() -> filmController.put(film));
	}


	@Test
	public void testCreateUserEmptyName() {
		User user = User.builder()
				.name("")
				.login("User login")
				.build();
		userController.create(user);
		assertEquals("User login", user.getName());
	}

	@Test
	public void testPutUser() {
		User user = User.builder()
				.id(100)
				.email("User email")
				.login("User login")
				.name("User name")
				.birthday(LocalDate.of(2002, 9, 1))
				.build();

		Assertions.assertThrows(NotFoundException.class, () -> userController.put(user));

		userController.create(user);
		Assertions.assertDoesNotThrow(() -> userController.put(user));
	}

}
