package dev.J.RepositoryForFamilies.Events;

import dev.J.RepositoryForFamilies.Users.EmailPasswordAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
@RestController
public class EventController {

    private final EventService eventService;



    private static int GLANCE_SIZE = 3;

    @GetMapping("events/api/member/glance")
    public List<EventGlance> getEventGlance(@RequestParam UUID groupId){
        List<EventGlance> glances = eventService.allEventsFromGroup(groupId,EventGlance.class);
        return glances.subList(0, Math.min(GLANCE_SIZE, glances.size()));
    }

    @GetMapping("/events/api/member/allevents")
    public List<EventDTO> allEvents(EmailPasswordAuthenticationToken auth, @RequestParam UUID groupId) {
        return eventService.allEvents(groupId, auth.getEmail());
    }


    @GetMapping("/events/api/member/events")
    public List<EventDTO> allEvents(@RequestParam UUID groupId,
                                 @RequestParam @DateTimeFormat(pattern = "YYYY-MM-DD") LocalDate ofWeek) {
        List<EventDTO> v =  eventService.allEventsFromGroupFromWeek(groupId,ofWeek,EventDTO.class);
        return v;
    }

    @GetMapping("/events/api/member/myevents")
    public List<Event.Details> myEvents(@RequestParam UUID groupId, EmailPasswordAuthenticationToken auth){
        return eventService.allUserEvents(auth.getEmail(),groupId);
    }

    public record EventBody(String description,
                            @DateTimeFormat(pattern = "YYYY-MM-DD") LocalDate eventDate,
                            LocalTime startTime,
                            LocalTime endTime,
                            String eventName) {}

    @PostMapping("/events/api/member/postevent")
    public ResponseEntity<UUID> postEvent(Authentication auth,
                                          @RequestParam UUID groupId,
                                          @RequestBody EventBody eventBody){

        UUID newEventID = eventService.createEvent(auth.getName(),groupId,eventBody);


        return ResponseEntity
                .ok(newEventID);
    }


    public record DeleteEventBody(String eventId) {}

    @PostMapping("/events/api/member/deleteevent")
    public ResponseEntity<Void> deleteEvent(Authentication auth,
                                            @RequestBody DeleteEventBody deleteEventBody,
                                            @RequestParam("groupId") UUID groupId){
        eventService.deleteEvent(auth.getName(),groupId,deleteEventBody);
        return ResponseEntity
                .ok()
                .build();
    }

}
