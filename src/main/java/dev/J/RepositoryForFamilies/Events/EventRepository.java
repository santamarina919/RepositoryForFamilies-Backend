package dev.J.RepositoryForFamilies.Events;

import jakarta.persistence.SqlResultSetMapping;
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
public interface EventRepository extends CrudRepository<Event, EventId> {

    Optional<Event> findByEventId(UUID eventId);

    @Query(
            value = "SELECT event_id,owner,description,date,start_time,end_time,group_id,name " +
                    "FROM event " +
                    "WHERE event.group_id = ?1 AND event.date >= (?2)::DATE AND event.date <= (?2)::DATE + 6 " +
                    "ORDER BY event.date",
            nativeQuery = true
    )
    <T> List<T> findAllByGroupIdAndWeek(UUID groupId, String weekStart, Class<T> clazz);

    @NativeQuery(
            value = "SELECT event_id,owner,description,date,start_time,end_time,group_id,name " +
                    "FROM event " +
                    "WHERE event.group_id = ?1 " +
                    "ORDER BY event.date ASC ",
            sqlResultSetMapping = "event-dto"
    )
    <T> List<T> findAllByGroupId(UUID groupId, Class<T> clazz);

    @Query(
            value = "SELECT * " +
                    "FROM event " +
                    "order by event.date",
            nativeQuery = true
    )
    <T> List<T> findAll(Class<T> clazz);

    @Query(
            value = "SELECT  event.event_id,event.name, event.date,event.start_time, event.end_time " +
                    "FROM event " +
                    "INNER JOIN reservation using (event_id) " +
                    "WHERE resource_id = ?1 AND " +
                    "event.date = ?2 AND NOT " +
                    "(" +
                    "(event.start_time < ?3 AND event.end_time <?4)" +
                    "OR " +
                    "(?3 < event.start_time AND ?4 < event.end_time)" +
                    ")",
            nativeQuery = true
    )
    List<Event.Details> doesCollisionExist(UUID resourceId, LocalDate reservationDate, LocalTime startTime, LocalTime endTime);

}
