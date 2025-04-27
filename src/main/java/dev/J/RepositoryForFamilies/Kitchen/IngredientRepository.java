package dev.J.RepositoryForFamilies.Kitchen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient,Ingredient.IngredientId> {


    @Modifying
    @Query(
            value = "INSERT INTO ingredient " +
                    "VALUES (?1,?2,?3,?4)",
            nativeQuery = true
    )
    void createIngredient(String mealName, String itemName, String unit, String quantity);

    List<Ingredient> findAllByMealId(String mealId);


    String meal(Meal meal);
}
