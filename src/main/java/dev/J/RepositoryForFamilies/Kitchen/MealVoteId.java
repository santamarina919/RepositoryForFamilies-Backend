package dev.J.RepositoryForFamilies.Kitchen;

import jakarta.persistence.Column;
import jakarta.persistence.ForeignKey;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MealVoteId {

    private LocalDate day;

    private MealType mealType;

    private String voter;
}
