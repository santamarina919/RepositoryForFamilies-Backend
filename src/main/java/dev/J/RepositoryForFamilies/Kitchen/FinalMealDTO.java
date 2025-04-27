package dev.J.RepositoryForFamilies.Kitchen;

import ch.qos.logback.core.rolling.DefaultTimeBasedFileNamingAndTriggeringPolicy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
public class FinalMealDTO {
    private String mealName;

    //todo create stream to map intermediate to final
    private List<DirectionDetails> directions;

    private HashSet<KitchenItem> ingredients;

    public FinalMealDTO(){
        this.mealName = null;
        this.directions = new ArrayList<>();
        this.ingredients = new HashSet<>();
    }

}
