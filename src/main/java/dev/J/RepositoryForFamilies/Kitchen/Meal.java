package dev.J.RepositoryForFamilies.Kitchen;

import dev.J.RepositoryForFamilies.Groups.Groups;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CollectionId;

import java.util.List;
import java.util.UUID;

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

    @Column(name = "owning_group", insertable = false,updatable = false)
    private UUID owningGroup;

    @JoinColumn(name = "owning_group")
    @ManyToOne
    private Groups creatingGroup;

    @Column(name = "time_to_prepare")
    private double timeToPrepare;




}
