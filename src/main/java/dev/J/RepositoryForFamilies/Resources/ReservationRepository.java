package dev.J.RepositoryForFamilies.Resources;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends CrudRepository<Reservation, UUID> {
    Optional<Reservation> findReservationByResourceIdAndDateAndApprovedTrue(UUID resourceId, LocalDate reservationDate);
}
