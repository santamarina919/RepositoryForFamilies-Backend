package dev.J.RepositoryForFamilies.Resources;

import dev.J.RepositoryForFamilies.Events.Event;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.ToString;

import javax.naming.Name;
import java.util.Set;
import java.util.UUID;

@IdClass(Reservation.ReservationId.class)
@Entity(name = "reservation")
public class Reservation {
    static record ReservationId (UUID eventId, UUID groupId, UUID resourceId){}

    @Id
    @Column(name = "event_id")
    private UUID eventId;
    @Id
    @Column(name = "group_id")
    private UUID groupId;
    @Id
    @Column(name = "resource_id")
    private UUID resourceId;

    @Nullable
    private Boolean approved;

    @Nullable
    @Column(name = "rejection_note")
    private String rejectionNote;


    public interface ReservationDTO {
        UUID getResourceId();

        Boolean getApproved();

        String getRejectionNote();
    }
}
