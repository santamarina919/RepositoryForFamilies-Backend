package dev.J.RepositoryForFamilies.Resources;

import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends CrudRepository<Reservation, UUID> {
    Optional<Reservation> findReservationByResourceIdAndDateAndApprovedTrue(UUID resourceId, LocalDate reservationDate);

    @NativeQuery("SELECT * " +
            "FROM reservation " +
            "WHERE reservation.resource_id = ?1")
    List<Reservation> reservationsFor(UUID resourceId);


    @NativeQuery(
            "SELECT * " +
            "FROM reservation " +
            "INNER JOIN event ON reservation.linked_event = event.event_id " +
            "INNER JOIN users ON user.email = reservation.user_id" +
            "WHERE reservation.resource_id = ?1"
    )
    List<ReservationAndEventPreview> fetchAllReservations(UUID resourceId);

    List<ReservationAndEventPreview> findAllByResourceId(UUID resourceId);

}
