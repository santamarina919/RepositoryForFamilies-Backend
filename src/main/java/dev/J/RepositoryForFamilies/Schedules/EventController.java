package dev.J.RepositoryForFamilies.Schedules;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@RequiredArgsConstructor
@CrossOrigin(origins = {"http://192.168.50.237:3000","http://localhost:3000"},allowCredentials = "true")
@RestController
public class EventController {

    private final EventService eventService;

    public record EventBody(String description,
                            @DateTimeFormat(pattern = "YYYY-MM-DD") LocalDate date,
                            LocalTime startTime,
                            LocalTime endTime) {}

    @PostMapping("/events/api/member/postevent")
    public ResponseEntity<Void> postEvent(Authentication auth,
                                          @RequestParam("groupId") String groupIdStr,
                                          @RequestBody EventBody eventBody){

        eventService.postEvent(auth.getName(),eventBody);


        return ResponseEntity
                .ok()
                .build();
    }


    public record DeleteEventBody(String eventId) {}

    @PostMapping("/events/api/member/deleteevent")
    public ResponseEntity<Void> deleteEvent(Authentication auth,
                                            @RequestBody DeleteEventBody deleteEventBody,
                                            @RequestParam("groupId") String groupIdStr){
        UUID groupId = UUID.fromString(groupIdStr);
        eventService.deleteEvent(auth.getName(),groupId,deleteEventBody);
        return ResponseEntity
                .ok()
                .build();
    }

}
