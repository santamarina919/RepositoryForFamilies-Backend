package dev.J.RepositoryForFamilies.Resources;

import dev.J.RepositoryForFamilies.Events.Event;
import dev.J.RepositoryForFamilies.Events.EventRepository;
import dev.J.RepositoryForFamilies.Events.EventService;
import dev.J.RepositoryForFamilies.Groups.GroupsService;
import dev.J.RepositoryForFamilies.Groups.MemberType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ResourceService
{

    private final ResourceRepository resourceRepository;

    private final GroupsService groupsService;

    private final EventRepository eventRepository;

    public List<Resource> allResourcesInGroup(UUID groupId) {
        return resourceRepository.fetchAllByGroupId(groupId);
    }


    @Transactional
    public void createResource(String owner, String name, String description,String type){
        UUID resourceId = UUID.randomUUID();
        //Resource r = new Resource(resourceId, owner, name, description,type);
        resourceRepository.createResource(resourceId,owner,name,description,type == null ? null : type.toUpperCase());
    }


    @Transactional
    public boolean deleteResource(UUID groupId, String userId, UUID resourceId){
        MemberType type = groupsService.fetchMemberType(groupId,userId);
        Resource resource = resourceRepository.findResourceByResourceId(resourceId).orElseThrow();
        if(type != MemberType.ADMIN && !resource.getOwner().equals(userId)){
            return false;
        }

        resourceRepository.deleteById(resourceId);


        return true;
    }

    @Transactional
    public boolean approveReservation(UUID reservationId, UUID resourceId, String userId){
        Resource resource = resourceRepository.findResourceByResourceId(resourceId).orElseThrow();
        if(!resource.getOwner().equals(userId)){
            return false;
        }

        resourceRepository.approveReservation(reservationId);
        return true;
    }

    @Transactional
    public boolean denyReservation(UUID reservationId, UUID resourceId, String userId, String reason) {
        Resource resource = resourceRepository.findResourceByResourceId(resourceId).orElseThrow();
        if (!resource.getOwner().equals(userId)) {
            return false;
        }

        if (reason == null) {
            reason = "Reservation has been denied by resource owner";
        }

        resourceRepository.denyReservation(reservationId, reason);
        return true;
    }


    @Transactional
    public List<Event.Details> reserveResource(UUID resourceId,String userId,UUID linkedEvent,UUID groupId){

        Event event = eventRepository.findByEventId(linkedEvent).orElseThrow();

        List<Event.Details> reservationCollision = hasReservationCollision(resourceId,event.getDate(),event.getStartTime(),event.getEndTime());
        if(reservationCollision != null && !reservationCollision.isEmpty()){
            return reservationCollision;
        }

       Resource resource = resourceRepository.findResourceByResourceId(resourceId).orElseThrow();
       if(!resource.getOwner().equals(userId)){
           resourceRepository.createReservation(linkedEvent,resourceId,null,null);
           return null;
       }



       resourceRepository.createReservation(linkedEvent,resourceId,true,null);
       return null;
    }

    public List<Event.Details> hasReservationCollision(UUID resourceId,  LocalDate reservationDate, LocalTime startTime, LocalTime endTime){
        List<Event.Details> collidingEvents = eventRepository.doesCollisionExist(resourceId,reservationDate,startTime,endTime);
        return collidingEvents;
    }

    @Transactional
    public boolean deleteReservation(UUID reservationId, UUID resourceId,UUID groupId, String userId){
//        UserType type = groupsService.fetchMemberType(groupId,userId);
//        Reservation r = reservationRepository.findById(reservationId).orElseThrow();
//        if(type != UserType.ADMIN && !r.getReservationOwner().getEmail().equals(userId)){
//            return false;
//        }
//
//        reservationRepository.deleteById(reservationId);
        return true;
    }




    /**
     * Returns n resources and the closes event for each of them
     * @param groupId
     * @param numOfResources
     * @return
     */
    public List<ResourceAvailability> glanceResources(UUID groupId, int numOfResources) {
        List<Resource> resourceList = resourceRepository.fetchNByGroupId(groupId,numOfResources);
        List<ResourceAvailability> availabilityList = new ArrayList<>();
        for(Resource resource : resourceList){
            List<Event> events = Collections.emptyList();//resource.getReservations();
            if(events.isEmpty()){
                availabilityList.add(
                        new ResourceAvailability(resource, ResourceAvailability.Availability.AVAILABLE,null)
                );
                return availabilityList;
            }

            Collections.sort(events);
            //If the current time is in the middle of an event that reserved the resource then
            //it is unavailable otherwise it is available
            Event firstEvent = events.getFirst();

            LocalDateTime now = LocalDateTime.now();

            LocalTime startTime = firstEvent.getStartTime();
            LocalDateTime eventStart = LocalDateTime.of(firstEvent.getDate(),startTime == null ? LocalTime.MIN : startTime);

            if(now.isBefore(eventStart)){
                availabilityList.add(
                        new ResourceAvailability(resource, ResourceAvailability.Availability.AVAILABLE,eventStart)
                );
            }
            else{
//TODO find out if this is needed
//                LocalTime endTime = firstEvent.getEndTime();
//                LocalDateTime eventEnd =
//                        LocalDateTime.of(firstEvent.getDate(),endTime == null ? LocalTime.MAX : endTime);
                availabilityList.add(
                        new ResourceAvailability(resource, ResourceAvailability.Availability.UNAVAILABLE,null)
                );
            }


        }
        return availabilityList;
    }

    public List<Reservation.ReservationDTO> allResourcesForEvent(UUID eventId) {
        return resourceRepository.fetchAllResourcesAttatchedToEvent(eventId);
    }

    public <T> Optional<T> fetchResource(UUID resourceId,Class<T> clazz){
        return resourceRepository.findByResourceId(resourceId,clazz);
    }
}
