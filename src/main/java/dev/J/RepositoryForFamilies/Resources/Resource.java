package dev.J.RepositoryForFamilies.Resources;

import dev.J.RepositoryForFamilies.Events.Event;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JoinColumnOrFormula;

import java.util.List;
import java.util.Set;
import java.util.UUID;



@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Resource {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "resource_id",insertable = false,updatable = false)
    private UUID resourceId;

    private String owner;

    private String name;

    private String description;

    private String type;

    @ManyToMany(targetEntity = Event.class)
    @JoinTable(
            name = "reservation",
            joinColumns = {
                @JoinColumn(name = "resource_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "event_id"),
                    @JoinColumn(name = "group_id")
            }
    )
    private Set<Event> events;

    @Getter
    @Builder
    public static class Details {

        UUID resourceId;

        String owner;

        String name;

        String description;

        String type;
    }
}
