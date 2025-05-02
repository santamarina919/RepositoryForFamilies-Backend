package dev.J.RepositoryForFamilies.Kitchen;

import lombok.*;

import java.util.stream.IntStream;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DirectionDetails {
    private Integer step;

    private String direction;

    private Boolean optional;

    public DirectionDetails(Direction d) {
        if(d != null) {
            this.step = d.getStep();
            this.direction = d.getDirectionString();
            this.optional = d.isOptional();

        }
        else{
            throw new RuntimeException("d cannot be null");
        }
    }
}

