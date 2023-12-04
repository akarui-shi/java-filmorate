package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private int id;
    @Email
    @NotNull
    @NotBlank
    private String email;
    @NotNull
    @NotBlank
    @Pattern(regexp = "\\w+")
    private String login;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    @PastOrPresent
    private LocalDate birthday;

}
