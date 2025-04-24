package dev.J.RepositoryForFamilies.Resources;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResourceRepository extends CrudRepository<Resource, UUID> {
    Optional<Resource> findResourceByResourceId(UUID resourceId);

    List<Resource> findAllByOwner(String ownerId);

    @Query( value =
            "SELECT new dev.J.RepositoryForFamilies.Resources.ResourceDetails(r.resourceId,r.owner,r.name,r.description,r.type) " +
            "FROM Resource r " +
            "INNER JOIN members m ON r.owner = m.userId " +
            "WHERE m.groupId = ?1 ")
    List<ResourceDetails> fetchAllByGroupId(UUID groupId);

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
                    "(event_id,resource_id,approved,rejection_note) " +
                    "VALUES (?1,?2,?3,?4)",
            nativeQuery = true)
    void createReservation(UUID eventId, UUID resourceId, Boolean approved, String rejectionNote);


    @Query(
            value = "SELECT * " +
                    "FROM reservation " +
                    "WHERE reservation.event_id = ?1 ",
            nativeQuery = true
    )
    List<Reservation.ReservationDTO> fetchAllResourcesAttatchedToEvent(UUID eventId);

    <T> Optional<T> findByResourceId(UUID resourceId, Class<T> clazz);
}
