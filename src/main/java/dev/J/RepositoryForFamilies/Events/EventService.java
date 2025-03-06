package dev.J.RepositoryForFamilies.Events;

import dev.J.RepositoryForFamilies.Groups.GroupsService;
import dev.J.RepositoryForFamilies.Groups.UserType;
import dev.J.RepositoryForFamilies.Users.EmailPasswordAuthenticationToken;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.data.convert.DtoInstantiatingConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;

    private final GroupsService userService;

    @Transactional
    public void postEvent(String userId, UUID uuid, EventController.EventBody body){
        UUID eventId = UUID.randomUUID();
        Event event = new Event(eventId,uuid,userId,body.description(),body.date(),body.startTime(),body.endTime(),body.name());
        eventRepository.save(event);
    }

    @Transactional
    public boolean deleteEvent(String userId,UUID groupId, EventController.DeleteEventBody body){
        UUID eventId = UUID.fromString(body.eventId());
        Event e = eventRepository.findByEventId(eventId).orElseThrow();
        UserType type = userService.fetchMemberType(groupId,userId);
        if(!e.getOwner().equals(userId) && type != UserType.ADMIN){
            return false;
        }
        eventRepository.delete(e);
        return true;
    }

    public <T> List<T> allEventsFromGroupFromWeek(UUID groupId, LocalDate weekStart, Class<T> clazz) {
        return eventRepository.findAllByGroupIdAndWeek(groupId,weekStart.toString(),clazz);
    }

    public List<EventDTO> allEvents(UUID groupId, String email) {
        List<EventDTO> events =  eventRepository.findAllByGroupId(groupId,EventDTO.class);
        for(EventDTO event : events){
            if(event.owner.equals(email)){
                event.setHasWriteAccess(true);
            }
        }
        return events;
    }
}
