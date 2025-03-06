package dev.J.RepositoryForFamilies.Schedules;

import com.sun.java.accessibility.util.EventID;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends CrudRepository<Event, EventId> {

    Optional<Event> findByEventId(UUID eventId);

    @Query(
            value = "SELECT user_id as EventOwner,description ,date as EventDate " +
                    "FROM event " +
                    "WHERE group_id = ?1 " +
                    "ORDER BY date " +
                    "LIMIT 10",
            nativeQuery = true)
    List<ScheduleGlance> fetchScheduleGlance(UUID groupId);
}
