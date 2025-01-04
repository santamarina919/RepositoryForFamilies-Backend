package dev.J.RepositoryForFamilies.Schedules;

import com.sun.java.accessibility.util.EventID;
import org.springframework.data.domain.Limit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends CrudRepository<Event, EventId> {

    Optional<Event> findByEventId(UUID eventId);

}
