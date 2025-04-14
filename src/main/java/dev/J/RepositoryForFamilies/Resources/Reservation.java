package dev.J.RepositoryForFamilies.Resources;

import dev.J.RepositoryForFamilies.Events.Event;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@Entity(name = "reservation")
@IdClass(Reservation.ReservationId.class)
public class Reservation implements Serializable {

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @Embeddable
    public static class ReservationId implements Serializable {
        UUID resourceId;
        UUID eventId;
    }

    @Id
    @Column(name = "resource_id")
    private UUID resourceId;

    @Id
    @Column(name = "event_id")
    private UUID eventId;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;

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

    public interface Details {
        Object getId();

        Event getEvent();

        Resource getResource();

        Boolean getApproved();

        String getRejectionNote();
    }
}
