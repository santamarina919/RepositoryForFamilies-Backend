package dev.J.RepositoryForFamilies.Resources;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResourceRepository extends CrudRepository<Resource, UUID> {
    Optional<Resource> findResourceByResourceId(UUID resourceId);

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

    @Query( value =
            "SELECT resource.resource_id, resource.owner, resource.name,resource.description, resource.type " +
            "FROM resource " +
            "INNER JOIN members ON members.user_id = resource.owner " +
            "WHERE members.group_id = ?1",
            nativeQuery = true)
    List<Resource> fetchAllByGroupId(UUID groupId);

    @Query( value =
            "SELECT resource.resource_id, resource.owner, resource.name,resource.description, resource.type " +
                    "FROM resource " +
                    "INNER JOIN members ON members.user_id = resource.owner " +
                    "WHERE members.group_id = ?1 " +
                    "LIMIT ?2",
            nativeQuery = true)
    List<Resource> fetchNByGroupId(UUID groupId,int n);

    @Modifying
    @NativeQuery(value = "INSERT INTO resource (resource_id,owner,name,description,type) VALUES (?1,?2,?3,?4,?5)")
    void createResource(UUID resourceId, String owner, String name, String description,String type);

    @Modifying
    @Query(value =
            "INSERT INTO reservation " +
                    "(event_id,group_id,resource_id,approved,rejection_note) " +
                    "VALUES (?1,?2,?3,?4,?5)",
            nativeQuery = true)
    void createReservation(UUID eventId, UUID groupId, UUID resourceId, Boolean approved, String rejectionNote);


    @Query(
            value = "SELECT * " +
                    "FROM reservation " +
                    "WHERE reservation.event_id = ?1 ",
            nativeQuery = true
    )
    List<Reservation.ReservationDTO> fetchAllResourcesAttatchedToEvent(UUID eventId);

    <T> Optional<T> findByResourceId(UUID resourceId, Class<T> clazz);
}
