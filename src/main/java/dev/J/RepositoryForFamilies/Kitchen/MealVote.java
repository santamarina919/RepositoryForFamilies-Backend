package dev.J.RepositoryForFamilies.Kitchen;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(MealVoteId.class)
@Entity(name = "Meal_Vote")
public class MealVote {



    @Id
    @Column
    private LocalDate day;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type")
    private MealType mealType;

    @Column(name = "meal_id")
    private String mealId;

    @Id
    @Column
    private String voter;

    @Column(name = "owning_group")
    private UUID owningGroup;





}
