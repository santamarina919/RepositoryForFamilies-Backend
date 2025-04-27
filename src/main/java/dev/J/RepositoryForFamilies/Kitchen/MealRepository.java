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
            "SELECT new dev.J.RepositoryForFamilies.Kitchen.IntermediateMealDTO(meal,dir,kitchenItem) " +
            "FROM Meal meal " +
            "INNER JOIN Direction dir on meal.name = dir.mealId " +
            "INNER JOIN Ingredient ingredient ON meal.name = ingredient.mealId " +
            "INNER JOIN Kitchen_Item kitchenItem ON ingredient.itemName = kitchenItem.name " +
            "WHERE meal.creatingGroup.id = ?1 "
    )
    List<IntermediateMealDTO> fetchAllMeals(UUID groupId);

    <T> List<T> findAllByOwningGroup(UUID groupId, Class<T> clazz);

    @Modifying
    @Query(
            value = "INSERT INTO meal " +
                    "VALUES (?1,?2,?3)",
            nativeQuery = true
    )
    void createMeal(String mealName, UUID groupId, double timeToPrepare);
}
