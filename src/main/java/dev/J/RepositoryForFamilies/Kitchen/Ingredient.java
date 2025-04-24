package dev.J.RepositoryForFamilies.Kitchen;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@IdClass(Ingredient.class)
@Entity
public class Ingredient {


    @Getter
    @Setter
    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    private static class IngredientId {
        private String mealId;

        private String itemId;
    }

    @Id
    @Column(name = "meal_id")
    private String mealId;

    @Id
    @Column(name = "item_name")
    private String itemName;

    @MapsId
    @JoinColumn(name = "meal_id")
    @ManyToOne
    private Meal meal;

    @MapsId
    @JoinColumn(name = "item_name")
    @ManyToOne
    private KitchenItem item;

    @Column
    private String unit;

    @Column
    private String quantity;

}
