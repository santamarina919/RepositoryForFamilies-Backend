package dev.J.RepositoryForFamilies.Resources;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.J.RepositoryForFamilies.Users.EmailPasswordAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
@RestController
public class ResourcesController {

    private final ResourceService resourceService;




    @GetMapping("/resources/api/member/all")
    public List<ResourceAndReservations> listAllResources(EmailPasswordAuthenticationToken auth,@RequestParam UUID groupId) {
        return resourceService.allResourcesAndReservations(auth,groupId);
    }

    public record CreateResourceBody(String resourceName, String description,String type){}


    @PostMapping("/resources/api/member/createresource")
    public void createResource(@RequestParam("groupId") String groupIdStr, EmailPasswordAuthenticationToken auth, @RequestBody CreateResourceBody body){
        UUID groupId = UUID.fromString(groupIdStr);
        resourceService.createResource(auth.getEmail(),body.resourceName(),body.description(), body.type().strip());
    }

    public record DeletedResourceBody(UUID resourceId){}

    @PostMapping("/resources/api/member/deleteresource")
    public void deleteResource(@RequestParam("groupId") String groupIdStr, EmailPasswordAuthenticationToken auth, @RequestBody DeletedResourceBody resource){
        UUID groupId = UUID.fromString(groupIdStr);
        resourceService.deleteResource(groupId,auth.getEmail(),resource.resourceId());
    }

    public record ReserveBody(String resourceId,
                              @DateTimeFormat(pattern = "YYYY-MM-DD") LocalDate date,
                              @JsonFormat(pattern = "HH:mm") LocalTime startTime,
                              @JsonFormat(pattern = "HH:mm") LocalTime endTime,
                              String notes,UUID linkedEvent){}

    @PostMapping("/resources/api/member/reserve")
    public void reserve(@RequestParam UUID groupId, EmailPasswordAuthenticationToken auth,
                        @RequestBody ReserveBody body) {
        UUID resourceId = UUID.fromString(body.resourceId);
        resourceService.reserveResource(resourceId, auth.getEmail(),body.date(),body.startTime(),body.endTime(),body.notes(),body.linkedEvent(),groupId);
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

    //pick 3 random resources
    //with those 3 return two items for each resource
    @GetMapping("resources/api/member/availability")
    public Map<String,AvailableBlock> availability(@RequestParam UUID groupId, EmailPasswordAuthenticationToken auth, @RequestParam int n){
        System.out.println("we are in controller");
        List<Resource> resources = resourceService.fetchN(n);
        Map<String ,AvailableBlock> availabilityMap = new HashMap<>();
        for(Resource r : resources){
            AvailableBlock block = resourceService.nextAvailability(r);
            availabilityMap.put(r.getName(),block);
        }
        return availabilityMap;
    }

}
