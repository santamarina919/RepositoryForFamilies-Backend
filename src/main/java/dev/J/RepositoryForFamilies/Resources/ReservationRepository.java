package dev.J.RepositoryForFamilies.Resources;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation,Reservation.ReservationId> {
    @Query(
            value = "SELECT * " +
                    "FROM reservation " +
                    "where reservation.event_id = ?1 AND reservation.group_id = ?2 AND " +
                    "reservation.resource_id = ?2",
            nativeQuery = true
    )
    Reservation fetchDetails(UUID eventId, UUID groupId, UUID resourceId);
}
