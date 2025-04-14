package dev.J.RepositoryForFamilies.Resources;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.J.RepositoryForFamilies.Events.Event;
import dev.J.RepositoryForFamilies.Users.EmailPasswordAuthenticationToken;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
@RestController
public class ResourcesController {

    private final ResourceService resourceService;


    private static final int NUM_OF_RESOURCES = 3;
    /**
     * Endpoint grabs n random resources and gives their current status.
     * Two statuses possible -> Available and Unavailable
     * Both have an attribute 'status end' which denotes when that status will change to the other
     * @return
     */
    @GetMapping("/resources/api/member/glance")
    private List<ResourceAvailability> resourceGlance(@RequestParam UUID groupId){
        return resourceService.glanceResources(groupId,NUM_OF_RESOURCES);
    }


    /**
     * Endpoint returns all the resources with associated reservations
     * Can be thought of as a map that maps a Resource -> List<Reservations>
     * @param auth
     * @param groupId
     * @return
     */
    @GetMapping("/resources/api/member/all")
    public List<Resource> listAllResources(EmailPasswordAuthenticationToken auth,@RequestParam UUID groupId) {

        return resourceService.allResourcesInGroup(groupId);
    }

    public record CreateResourceBody(String resourceName, String description,String type){}

    @PostMapping("/resources/api/member/createresource")
    public void createResource(@RequestParam UUID groupId, EmailPasswordAuthenticationToken auth, @RequestBody CreateResourceBody body){
        resourceService.createResource(auth.getEmail(),body.resourceName(),body.description(), body.type().strip());
    }

    public record DeletedResourceBody(UUID resourceId){}

    @PostMapping("/resources/api/member/deleteresource")
    public void deleteResource(@RequestParam("groupId") String groupIdStr, EmailPasswordAuthenticationToken auth, @RequestBody DeletedResourceBody resource){
        UUID groupId = UUID.fromString(groupIdStr);
        resourceService.deleteResource(groupId,auth.getEmail(),resource.resourceId());
    }

    record ReserveResponse(ResourceReservationDetails resourceDetails, @Nullable List<Event.Details> collidingEvents){}

    @PostMapping("/resources/api/member/reserve")
    public ResponseEntity<ReserveResponse> reserve(EmailPasswordAuthenticationToken auth, @RequestParam UUID groupId,
                                               @RequestParam UUID resourceId, @RequestParam UUID linkedEvent) {
        List<Event.Details> collidingEvents = resourceService.reserveResource(resourceId, auth.getEmail(),linkedEvent,groupId);
        Resource.Details details = resourceService.fetchResource(resourceId,Resource.Details.class).orElseThrow();
        ResourceReservationDetails reservationDetails = new ResourceReservationDetails(details,false,null);
        return collidingEvents != null && !collidingEvents.isEmpty() ?

                ResponseEntity.badRequest().body(new ReserveResponse(reservationDetails,collidingEvents)) :

                ResponseEntity.ok(new ReserveResponse(reservationDetails,null));
    }

    public record RejectionBody(UUID reservationId, UUID resourceId){}

    @PostMapping("resources/api/member/rejectreservation")
    public void rejectReservation(@RequestParam(name = "groupId") String groupIdStr, EmailPasswordAuthenticationToken auth,
                                  @RequestBody RejectionBody body,String rejectionNote){
        resourceService.denyReservation(body.reservationId(),body.resourceId(),auth.getEmail(),rejectionNote);
    }

    public record ApprovalBody(UUID reservationId, UUID resourceId){}

    @PostMapping("resources/api/member/approvereservation")
    public void approveReservation(@RequestParam(name = "groupId") String groupIdStr, EmailPasswordAuthenticationToken auth,
                                   @RequestBody ApprovalBody body){
        resourceService.approveReservation(body.reservationId(),body.resourceId(),auth.getEmail());
    }

    public record DeleteReservationBody(UUID reservationId){}

    //resource id is not needed right now but left it in just in case i need it
    @PostMapping("resources/api/member/deletereservation")
    public void deleteReservation(@RequestParam(name = "groupId") UUID groupId, EmailPasswordAuthenticationToken auth,
                                  @RequestBody DeleteReservationBody body){
        resourceService.deleteReservation(body.reservationId(),null,groupId,auth.getEmail());
    }


    @GetMapping("test")
    public Reservation res(){
        return resourceService.test();
    }


}
