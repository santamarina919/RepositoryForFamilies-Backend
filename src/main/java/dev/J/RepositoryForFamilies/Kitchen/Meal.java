package dev.J.RepositoryForFamilies.Kitchen;

import dev.J.RepositoryForFamilies.Groups.Groups;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CollectionId;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Meal {

    @Id
    @Column
    private String name;

    @ManyToOne
    private Groups owningGroup;

    @Column(name = "time_to_prepare")
    private double timeToPrepare;

}
