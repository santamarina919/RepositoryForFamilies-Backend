package dev.J.RepositoryForFamilies.Resources;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.management.ValueExp;
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

    @Modifying
    @NativeQuery(value = "INSERT INTO resource (resource_id,owner,name,description,type) VALUES (?1,?2,?3,?4,?5)")
    void createResource(UUID resourceId, String owner, String name, String description,String type);

    @Modifying
    @Query(value =
            "INSERT INTO reservation " +
                    "(reservation_id,resource_id,user_id,group_id,linked_event,date,start_time,end_time,notes,approved,rejection_note) " +
                    "VALUES (?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11)",
            nativeQuery = true)
    void createReservation(UUID reservationId, UUID resourceId, String userId, UUID groupId,
                           UUID linkedEvent, LocalDate date, LocalTime startTime, LocalTime endTime, String notes,
                           boolean approved, String rejectionNote);

    @NativeQuery(value =
            "SELECT * " +
            "FROM resource " +
            "LIMIT ?1")
    List<Resource> fetchN(int n);
}
