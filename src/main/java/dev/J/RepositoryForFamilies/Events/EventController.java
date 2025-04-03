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

    public record EventBody(String description,
                            @DateTimeFormat(pattern = "YYYY-MM-DD") LocalDate date,
                            LocalTime startTime,
                            LocalTime endTime,
                            String name) {}


    @GetMapping("events/api/member/glance")
    public List<EventGlance> getEventGlance(@RequestParam UUID groupId){
        return eventService.allEventsFromGroup(groupId,EventGlance.class);
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

    @PostMapping("/events/api/member/postevent")
    public ResponseEntity<Void> postEvent(Authentication auth,
                                          @RequestParam("groupId") String groupIdStr,
                                          @RequestBody EventBody eventBody){

        eventService.postEvent(auth.getName(),UUID.fromString(groupIdStr),eventBody);


        return ResponseEntity
                .ok()
                .build();
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
