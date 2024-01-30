package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class User {
    private int id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Pattern(regexp = "\\w+")
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;
    private Set<Integer> friends;

    public Map<String, Object> userRowMap() {
        Map<String, Object> userRow = new HashMap<>();
        userRow.put("USER_EMAIL", email);
        userRow.put("USER_LOGIN", login);
        userRow.put("USER_NAME", name);
        userRow.put("USER_BIRTHDAY", birthday);
        return userRow;
    }
}
