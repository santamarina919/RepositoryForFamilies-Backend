package dev.J.RepositoryForFamilies.Kitchen;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@IdClass(Direction.class)
@Entity
public class Direction {


    public static class DirectionId {
        private String mealId;

        private int step;
    }

    @Id
    @Column(name = "meal_Id")
    private String mealId;

    @Id
    @Column
    private int step;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "meal_id")
    private Meal meal;

    @Column(name = "direction_str")
    private String directionString;

}
