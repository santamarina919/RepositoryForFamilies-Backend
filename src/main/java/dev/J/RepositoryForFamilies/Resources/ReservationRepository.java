package dev.J.RepositoryForFamilies.Resources;

import dev.J.RepositoryForFamilies.Events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;

import java.lang.annotation.Native;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation,Reservation.ReservationId> {


    @Query(value =
            "SELECT new dev.J.RepositoryForFamilies.Resources.ReservationDetails(r.resourceId,r.approved,r.rejectionNote,r.event)" +
            "FROM  Reservation r " +
            "WHERE r.event.groupId = ?1"
    )
    List<ReservationDetails> allReservations(UUID groupId);


    @Modifying
    @Query(value =
            "UPDATE Reservation r " +
            "SET r.approved = true " +
            "WHERE r.resourceId = ?1 AND r.eventId = ?2")
    void approveReservation(UUID resourceId, UUID eventId);

    @Modifying
    @Query(value =
            "UPDATE Reservation r " +
            "SET r.approved = false, r.rejectionNote = ?3 " +
            "WHERE r.resourceId = ?1 AND r.eventId = ?2"
    )
    void rejectReservation(UUID resourceId, UUID eventId, String reason);


}
