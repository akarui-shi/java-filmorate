package ru.yandex.practicum.filmorate;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmValidationTests {

    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void correctDataTest() {
        Film film = Film.builder()
                .name("Film name")
                .description("Film description")
                .releaseDate(LocalDate.of(2021, 9, 1))
                .duration(100)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void emptyNameTest() {
        Film film = Film.builder()
                .description("Film description")
                .releaseDate(LocalDate.of(2021, 9, 1))
                .duration(100)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        System.out.println(violations);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void emptyDescriptionTest() {
        Film film = Film.builder()
                .name("Film name")
                .releaseDate(LocalDate.of(2021, 9, 1))
                .duration(100)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void invalidSizeDescriptionTest() {
        Film film = Film.builder()
                .name("Film name")
                .description(Arrays.toString(Arrays.copyOf(new int[]{0}, 201)))
                .releaseDate(LocalDate.of(2021, 9, 1))
                .duration(100)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void emptyReleaseDateTest() {
        Film film = Film.builder()
                .name("Film name")
                .description("Film description")
                .duration(100)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void emptyDurationTest() {
        Film film = Film.builder()
                .name("Film name")
                .description("Film description")
                .releaseDate(LocalDate.of(2021, 9, 1))
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void negativeDurationTest() {
        Film film = Film.builder()
                .name("Film name")
                .description("Film description")
                .releaseDate(LocalDate.of(2021, 9, 1))
                .duration(-100)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }
}
