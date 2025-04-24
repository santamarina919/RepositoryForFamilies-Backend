package dev.J.RepositoryForFamilies.Resources;

import dev.J.RepositoryForFamilies.Events.Event;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JoinColumnOrFormula;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;



@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Resource implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "resource_id",insertable = false,updatable = false)
    private UUID resourceId;

    private String owner;

    private String name;

    private String description;

    private String type;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "reservation",
            joinColumns = {
                @JoinColumn(name = "resource_id"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "event_id")
            }
    )
    private Set<Event> events;

}
