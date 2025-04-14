package dev.J.RepositoryForFamilies.Resources;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;

import java.lang.annotation.Native;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Reservation.ReservationId> {
    @Query(value =
            "SELECT r " +
            "FROM reservation r " +
            "INNER JOIN Event event on r.eventId = event.eventId " +
            "INNER JOIN resource on r.resourceId = resource.resourceId")
    List<Reservation> reservations();
}
