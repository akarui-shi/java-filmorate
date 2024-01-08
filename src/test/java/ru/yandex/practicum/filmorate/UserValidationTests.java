package ru.yandex.practicum.filmorate;

import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserValidationTests {
    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void correctDataTest() {
        User user = User.builder()
                .email("userEmail@yandex.ru")
                .login("User_login")
                .name("User name")
                .birthday(LocalDate.of(2002, 9, 1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void emptyEmailTest() {
        User user = User.builder()
                .email("")
                .login("User_login")
                .name("User name")
                .birthday(LocalDate.of(2002, 9, 1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void incorrectEmailTest() {
        User user = User.builder()
                .email("User email")
                .login("User_login")
                .name("User name")
                .birthday(LocalDate.of(2002, 9, 1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        user.setEmail("userEmailyandex.ru");
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void emptyLoginTest() {
        User user = User.builder()
                .email("userEmail@yandex.ru")
                .name("User name")
                .birthday(LocalDate.of(2002, 9, 1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void  incorrectLoginTest() {
        User user = User.builder()
                .email("userEmail@yandex.ru")
                .login("User login")
                .name("User name")
                .birthday(LocalDate.of(2002, 9, 1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void  emptyBirthdayTest() {
        User user = User.builder()
                .email("userEmail@yandex.ru")
                .login("User_login")
                .name("User name")
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void  incorrectBirthdayTest() {
        User user = User.builder()
                .email("userEmail@yandex.ru")
                .login("User_login")
                .name("User name")
                .birthday(LocalDate.of(2027, 1, 2))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }
}
