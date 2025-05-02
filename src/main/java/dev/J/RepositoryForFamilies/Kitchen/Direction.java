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
public class Direction implements Comparable<Direction>{


    @Id
    @Column(name = "meal_Id")
    private String mealId;

    @Id
    @Column
    private int step;

    @Column
    private boolean optional;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "meal_id")
    private Meal meal;

    @Column(name = "direction_str")
    private String directionString;

    @Override
    public int compareTo(Direction o) {
        return this.step - o.step;
    }

}
