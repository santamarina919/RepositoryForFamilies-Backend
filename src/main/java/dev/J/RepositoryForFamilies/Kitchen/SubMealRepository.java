package dev.J.RepositoryForFamilies.Kitchen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.SubmissionPublisher;

@Repository
public interface SubMealRepository extends JpaRepository<SubMeal,SubMeal.SubMealId> {
    @Modifying
    @Query(
            value = "INSERT INTO sub_meal " +
                    "VALUES (?1,?2,?3)",
            nativeQuery = true
    )
    void createMeal(String primaryMeal, String subMeal, boolean optional);

    List<SubMeal> findAllByPrimaryMeal(String mealId);

}
