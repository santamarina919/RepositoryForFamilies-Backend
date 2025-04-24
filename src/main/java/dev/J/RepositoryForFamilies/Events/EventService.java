package dev.J.RepositoryForFamilies.Events;

import dev.J.RepositoryForFamilies.Groups.GroupsService;
import dev.J.RepositoryForFamilies.Groups.MemberType;
import dev.J.RepositoryForFamilies.Resources.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;

    private final GroupsService userService;

    private final ResourceService resourceService;

    @Transactional
    public UUID createEvent(String userId, UUID groupId, EventController.EventBody body){
        Event event = new Event(null,groupId ,userId,body.description(),body.eventDate(),body.startTime(),body.endTime(),body.eventName());
        eventRepository.save(event);
        return event.getEventId();
    }

    @Transactional
    public boolean deleteEvent(String userId,UUID groupId, EventController.DeleteEventBody body){
        UUID eventId = UUID.fromString(body.eventId());
        Event e = eventRepository.findByEventId(eventId).orElseThrow();
        MemberType type = userService.fetchMemberType(groupId,userId);
        if(!e.getOwner().equals(userId) && type != MemberType.ADMIN){
            return false;
        }
        eventRepository.deleteAssociatedReservations(e.getEventId());
        eventRepository.delete(e);
        return true;
    }

    public <T> List<T> allEventsFromGroupFromWeek(UUID groupId, LocalDate weekStart, Class<T> clazz) {
        return eventRepository.findAllByGroupIdAndWeek(groupId,weekStart.toString(),clazz);
    }

    public List<EventDTO> allEvents(UUID groupId, String email) {
        List<EventDTO> events =  eventRepository.findAllByGroupId(groupId,EventDTO.class);
        for(EventDTO event : events){
            if(event.getOwner().equals(email)){
                event.setHasWriteAccess(true);
            }
            List<Reservation.ReservationDTO> linkedResources = resourceService.allResourcesForEvent(event.getEventId());
            for(Reservation.ReservationDTO res : linkedResources){
                ResourceDetails resource = resourceService.fetchResource(res.getResourceId(), ResourceDetails.class).orElseThrow();
                event.getReservedResources().add(new ResourceReservationDetails(resource,res.getApproved(),res.getRejectionNote()));
            }
        }
        return events;
    }

    public <T> List<T> allEventsFromGroup(UUID groupId, Class<T> clazz){
        return eventRepository.findAllByGroupId(groupId,clazz);
    }

    public Optional<Event> fetchEventById(UUID eventId){
        return eventRepository.findByEventId(eventId);
    }

    public List<Event.Details> doesCollisionExist(UUID resourceId, LocalDate reservationDate, LocalTime startTime, LocalTime endTime) {
        return eventRepository.doesCollisionExist(resourceId,reservationDate,startTime,endTime);
    }

    public List<Event.Details> allUserEvents(String email, UUID groupId) {
        return eventRepository.fetchAllEventsFromUser(email,groupId);
    }
}
