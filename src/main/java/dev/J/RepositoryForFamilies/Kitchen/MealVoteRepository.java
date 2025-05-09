package dev.J.RepositoryForFamilies.Kitchen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MealVoteRepository extends JpaRepository<MealVote,MealVoteId> {


     @Query(value = "SELECT vote " +
                    "FROM Meal_Vote vote " +
                    "WHERE vote.mealType = ?1 AND vote.day = ?2 AND vote.owningGroup = ?3"
     )
     List<MealVote> fetchVotesForDay(MealType mealType, LocalDate date, UUID groupId);

}
