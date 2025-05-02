package dev.J.RepositoryForFamilies.Kitchen;


import org.hibernate.type.descriptor.sql.internal.NativeOrdinalEnumDdlTypeImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MealRepository extends JpaRepository<Meal,String> {

    @Query(value =
            "SELECT new dev.J.RepositoryForFamilies.Kitchen.MealDetails(meal) " +
            "FROM Meal meal " +
            "WHERE meal.creatingGroup.id = ?1"
    )
    List<MealDetails> fetchAllMeals(UUID groupId);


    @Query(value =
            "SELECT new dev.J.RepositoryForFamilies.Kitchen.DirectionDetails(direction.step,direction.directionString,direction.optional) " +
            "FROM Direction direction " +
            "WHERE direction.meal.name = ?1"
    )
    List<DirectionDetails> fetchMealDirections(String mealId);


    <T> List<T> findAllByOwningGroup(UUID groupId, Class<T> clazz);

    @Modifying
    @Query(
            value = "INSERT INTO meal " +
                    "VALUES (?1,?2,?3)",
            nativeQuery = true
    )
    void createMeal(String mealName, UUID groupId, double timeToPrepare);

    @Query(
            value = "SELECT COUNT(*) " +
                    "FROM meal " +
                    "WHERE meal.owning_group = ?1",
            nativeQuery = true
    )
    int fetchMealCount(UUID groupId);


    @Query(
            value =
                "SELECT new dev.J.RepositoryForFamilies.Kitchen.KitchenItem(item.name) " +
                "FROM Kitchen_Item item " +
                "INNER JOIN Ingredient i ON i.meal.name = ?1 "
    )
    List<KitchenItem> fetchIngredients(String mealId);
}
