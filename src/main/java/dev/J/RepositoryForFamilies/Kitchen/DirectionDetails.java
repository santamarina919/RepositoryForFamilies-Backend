package dev.J.RepositoryForFamilies.Kitchen;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DirectionDetails {
    private int step;

    private String direction;

    private boolean optional;
}

