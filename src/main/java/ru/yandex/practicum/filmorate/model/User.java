package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.NonFinal;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @NonFinal
    @Setter
    private Long id;

    @Email
    @NotBlank
    @Size(max = 50)
    private String email;

    @NotBlank
    @Pattern(regexp = "\\S*$")
    @Size(max = 50)
    private String login;

    @NonFinal
    @Size(max = 50)
    @Setter
    private String name;

    @Past
    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();
}
