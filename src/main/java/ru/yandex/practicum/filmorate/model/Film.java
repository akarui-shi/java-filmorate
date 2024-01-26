package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validation.ReleaseDateValidation;

import javax.validation.constraints.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class Film {
    private Integer id;
    @NotBlank
    private final String name;
    @NotBlank
    @Size(max = 200)
    private final String description;
    @NotNull
    @ReleaseDateValidation
    private final LocalDate releaseDate;
    @NotNull
    @Positive
    private final int duration;
    private final Mpa mpa;
    private Set<Genre> genres = new HashSet<>();
    private final int likes;

    public Film(Integer id, String name, String description, LocalDate releaseDate,
                Integer duration, Mpa mpa, int likes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.likes = likes;
    }

    public Map<String, Object> filmToMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("FILM_NAME", name);
        values.put("FILM_DESCRIPTION", description);
        values.put("FILM_RELEASE_DATE", releaseDate);
        values.put("FILM_DURATION", duration);
        values.put("MPA_ID", mpa.getId());
        return values;
    }
}
