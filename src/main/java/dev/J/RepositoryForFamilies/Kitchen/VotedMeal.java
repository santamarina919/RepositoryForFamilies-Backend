package dev.J.RepositoryForFamilies.Kitchen;

import java.time.LocalDate;

public record VotedMeal(LocalDate date, MealType type, Meal meal) {
}
