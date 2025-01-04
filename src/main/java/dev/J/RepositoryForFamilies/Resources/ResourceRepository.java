package dev.J.RepositoryForFamilies.Resources;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, UUID> {
    Resource findResourceByResourceId(UUID resourceId);

    @Modifying
    @Query(value =
            "UPDATE reservation " +
            "SET approved = true " +
            "WHERE reservation_id = ?1",
            nativeQuery = true)
    void approveReservation(UUID reservationId);

    @Modifying
    @Query(
        value = "UPDATE reservation " +
                "SET approved = false," +
                "rejection_note = ?2 " +
                "WHERE reservation_id = ?1",
            nativeQuery = true
    )
    void denyReservation(UUID reservationId, String reason);

    List<Resource> findAllByOwner(String ownerId);
}
