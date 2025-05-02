package dev.J.RepositoryForFamilies.Kitchen;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FullMealDTO {
    private String mealName;

    private double timeToPrepare;

    //todo create stream to map intermediate to final
    private List<DirectionDetails> directions;

    private List<KitchenItem> ingredients;


}
