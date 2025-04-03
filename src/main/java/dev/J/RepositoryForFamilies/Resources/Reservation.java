package dev.J.RepositoryForFamilies.Resources;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.util.UUID;

@IdClass(Reservation.ReservationId.class)
@Entity
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
}
