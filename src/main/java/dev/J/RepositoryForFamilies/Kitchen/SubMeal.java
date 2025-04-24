package dev.J.RepositoryForFamilies.Kitchen;

import com.fasterxml.jackson.core.sym.NameN;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@IdClass(SubMeal.SubMealId.class)
@Entity(name = "Sub_Meal")
public class SubMeal {

    public static class SubMealId{
        private String primaryMeal;

        private String subMeal;
    }

    @Id
    @Column(name = "primary_meal")
    private String primaryMeal;

    @Id
    @Column(name = "sub_meal")
    private String subMeal;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "primary_meal")
    private Meal mainMeal;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "sub_meal")
    private Meal recursiveMeal;


    private boolean optional;
}
