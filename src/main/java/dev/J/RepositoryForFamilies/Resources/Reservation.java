package dev.J.RepositoryForFamilies.Resources;

import dev.J.RepositoryForFamilies.Events.Event;
import dev.J.RepositoryForFamilies.Users.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.naming.Name;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;


//A reservation for a resource can only be approved by the owner of said resource
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID reservationId;

    private UUID resourceId;

    @ManyToOne
    @JoinColumn(name = "user_Id",referencedColumnName = "email")
    private Users reservationOwner;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "linked_event",referencedColumnName = "event_id"),
            @JoinColumn(name = "group_id",referencedColumnName = "group_id")
    })
    private Event linkedEvent;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private String notes;

    private boolean approved;

    private String rejectionNote;



}
