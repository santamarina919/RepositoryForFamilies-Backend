package dev.J.RepositoryForFamilies.Kitchen;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class MealDetails {

    private String name;

    private UUID owningGroup;

    private double timeToPrepare;

    public MealDetails(Meal meal){
        this.name = meal.getName();
        this.owningGroup = meal.getOwningGroup();
        this.timeToPrepare = meal.getTimeToPrepare();
    }

}
