package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
class FilmorateApplicationTests {
	private final FilmController filmController = new FilmController();
	private final UserController userController = new UserController();

	@Test
	public void testCreateFilmReleaseDate() {
		Film film = new Film();
		film.setReleaseDate(LocalDate.of(2021, 9, 1));
		Assertions.assertDoesNotThrow(() -> filmController.create(film));

		film.setReleaseDate(LocalDate.of(1895, 12, 27));
		Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
	}

	@Test
	public void testPutFilm() {
		Film film = new Film();
		film.setId(100);
		film.setName("Film name");
		film.setDescription("Film description");
		film.setReleaseDate(LocalDate.of(2021, 9, 1));
		film.setDuration(100);

		Assertions.assertThrows(ValidationException.class, () -> filmController.put(film));

		filmController.create(film);
		Assertions.assertDoesNotThrow(() -> filmController.put(film));
	}


	@Test
	public void testCreateUserEmptyName() {
		User user = new User();
		user.setName("");
		user.setLogin("User Login");
		userController.create(user);
		Assertions.assertEquals("User Login", user.getName());
	}

	@Test
	public void testPutUser() {
		User user = new User();
		user.setId(100);
		user.setName("User name");
		user.setLogin("User login");
		user.setEmail("User Email");
		user.setBirthday(LocalDate.of(2002, 9, 1));

		Assertions.assertThrows(ValidationException.class, () -> userController.put(user));

		userController.create(user);
		Assertions.assertDoesNotThrow(() -> userController.put(user));
	}

}
