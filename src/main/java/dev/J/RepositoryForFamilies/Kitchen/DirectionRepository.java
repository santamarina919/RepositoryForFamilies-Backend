package dev.J.RepositoryForFamilies.Kitchen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DirectionRepository extends JpaRepository<Direction,DirectionId> {

    @Modifying
    @Query(
        value = "INSERT INTO direction " +
                "VALUES (?1,?2,?3,?4)",
        nativeQuery = true)
    void makeDirection(String mealId, int step, String directionString, boolean optional);


    List<Direction> findAllByMealId(String mealId);
}
