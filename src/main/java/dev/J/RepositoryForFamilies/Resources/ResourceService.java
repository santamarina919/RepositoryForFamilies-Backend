package dev.J.RepositoryForFamilies.Resources;

import dev.J.RepositoryForFamilies.Events.Event;
import dev.J.RepositoryForFamilies.Groups.GroupsService;
import dev.J.RepositoryForFamilies.Groups.UserType;
import dev.J.RepositoryForFamilies.Users.EmailPasswordAuthenticationToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CollectionId;
import org.springframework.cglib.core.Local;
import org.springframework.data.convert.DtoInstantiatingConverter;
import org.springframework.stereotype.Service;

import java.time.Duration;
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

    public List<Resource> allResoucesInGroup(UUID groupId) {
        return resourceRepository.fetchAllByGroupId(groupId);
    }


    //TODO: fix
    public List<Void> allResourcesAndReservations(EmailPasswordAuthenticationToken auth, UUID groupId){
//        List<Resource> resources = allResoucesInGroup(groupId);
//        List<ResourceAndReservations> allEntities = new ArrayList<>();
//        for(Resource  r : resources){
//            List<ReservationAndEventPreview> reservations = reservationRepository.findAllByResourceId(r.getResourceId());
//            List<ResourceAndReservations.ReservationDTO> reservationDTOS = reservations.stream()
//                    .map(preview -> {
//                        if(r.getOwner().equals(auth.getEmail())){
//                            return ResourceAndReservations.ReservationDTO.withWriteAccess(preview);
//                        }
//                        else{
//                            return ResourceAndReservations.ReservationDTO.withoutWriteAccess(preview);
//                        }
//                    })
//                    .toList();
//            allEntities.add(new ResourceAndReservations(r,reservationDTOS));
//        }
//        return allEntities;
        return null;
    }

    public List<Resource> allResourcesFrom(String ownerId){
        return resourceRepository.findAllByOwner(ownerId);
    }


    @Transactional
    public void createResource(String owner, String name, String description,String type){
        UUID resourceId = UUID.randomUUID();
        //Resource r = new Resource(resourceId, owner, name, description,type);
        resourceRepository.createResource(resourceId,owner,name,description,type == null ? null : type.toUpperCase());
    }


    @Transactional
    public boolean deleteResource(UUID groupId, String userId, UUID resourceId){
        UserType type = groupsService.fetchMemberType(groupId,userId);
        Resource resource = resourceRepository.findResourceByResourceId(resourceId).orElseThrow();
        if(type != UserType.ADMIN && !resource.getOwner().equals(userId)){
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
    public boolean denyReservation(UUID reservationId, UUID resourceId, String userId, String reason){
        Resource resource = resourceRepository.findResourceByResourceId(resourceId).orElseThrow();
        if(!resource.getOwner().equals(userId)){
            return false;
        }

        if(reason == null){
            reason = "Reservation has been denied by resource owner";
        }

        resourceRepository.denyReservation(reservationId,reason);
        return true;
    }


    @Transactional
    public void requestReservation(UUID resourceId,
                                   String userId,
                                   LocalDate reservationDate,
                                   LocalTime startTime,
                                   LocalTime endTime,
                                   String notes,
                                   UUID linkedEvent,
                                   UUID groupId){
        resourceRepository.createReservation(
                UUID.randomUUID(),resourceId,userId,groupId,linkedEvent,reservationDate,startTime,endTime,notes,
                false,null)
        ;

    }


    @Transactional
    public Object reserveResource(UUID resourceId,
                                   String userId,
                                   LocalDate reservationDate,
                                   LocalTime startTime,
                                   LocalTime endTime,
                                   String notes,
                                   UUID linkedEvent,
                                   UUID groupId){
       Resource resource = resourceRepository.findResourceByResourceId(resourceId).orElseThrow();
       if(!resource.getOwner().equals(userId)){
            requestReservation(resourceId,userId,reservationDate,startTime,endTime,notes,linkedEvent,groupId);
           return null;
       }
       Object reservationCollision = hasReservationCollision(resourceId,reservationDate);
       if(reservationCollision != null){
           return reservationCollision;
       }
       if(notes == null) {
           notes = "Reserved by resource owner for this period of time";
       }

       resourceRepository.createReservation(UUID.randomUUID(),resourceId,userId,groupId,linkedEvent,reservationDate,
               startTime,endTime,notes,true,null);

       return null  ;
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

    public Object hasReservationCollision(UUID resourceId,  LocalDate reservationDate){
        return null;
    }


    public List<ResourceAvailability> glanceResources(UUID groupId, int numOfResources) {
        List<Resource> resourceList = resourceRepository.fetchNByGroupId(groupId,numOfResources);
        List<ResourceAvailability> availabilityList = new ArrayList<>();
        for(Resource resource : resourceList){
            List<Event> events = resource.getReservations();
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
                //TODO available
                //TODO until event start
                availabilityList.add(
                        new ResourceAvailability(resource, ResourceAvailability.Availability.AVAILABLE,eventStart)
                );
            }
            else{
                LocalTime endTime = firstEvent.getEndTime();
                LocalDateTime eventEnd =
                        LocalDateTime.of(firstEvent.getDate(),endTime == null ? LocalTime.MAX : endTime);
                availabilityList.add(
                        new ResourceAvailability(resource, ResourceAvailability.Availability.UNAVAILABLE,null)
                );
            }


        }
        return availabilityList;
    }
}
