package dev.J.RepositoryForFamilies.Kitchen;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IntermediateMealDTO {

    private String name;

    private DirectionDetails direction;

    private KitchenItem item;

    public IntermediateMealDTO(Meal m, Direction d,KitchenItem i){
        this.name = m.getName();
        this.direction = new DirectionDetails();
        this.direction.setStep(d.getStep());
        this.direction.setDirection(d.getDirectionString());
        this.item = i;
    }
}
