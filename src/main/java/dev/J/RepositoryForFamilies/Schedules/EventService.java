package dev.J.RepositoryForFamilies.Schedules;

import dev.J.RepositoryForFamilies.Groups.GroupsService;
import dev.J.RepositoryForFamilies.Groups.UserType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;

    private final GroupsService userService;

    @Transactional
    public void postEvent(String userId, EventController.EventBody body){
        UUID eventId = UUID.randomUUID();
        Event event = new Event(eventId,userId,body.description(),body.date(),body.startTime(),body.endTime());
        eventRepository.save(event);
    }

    @Transactional
    public boolean deleteEvent(String userId,UUID groupId, EventController.DeleteEventBody body){
        UUID eventId = UUID.fromString(body.eventId());
        Event e = eventRepository.findByEventId(eventId).orElseThrow();
        UserType type = userService.fetchMemberType(groupId,userId);
        if(!e.getUserId().equals(userId) && type != UserType.ADMIN){
            return false;
        }
        eventRepository.delete(e);
        return true;
    }

}
