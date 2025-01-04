package dev.J.RepositoryForFamilies.Resources;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ReservationRepository extends CrudRepository<Reservation, UUID> {
}
