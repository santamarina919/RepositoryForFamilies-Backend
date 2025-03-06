package dev.J.RepositoryForFamilies.Resources;

import dev.J.RepositoryForFamilies.Groups.GroupsService;
import dev.J.RepositoryForFamilies.Groups.UserType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ResourceService
{

    private final ResourceRepository resourceRepository;

    private final ReservationRepository reservationRepository;

    private final GroupsService groupsService;

    public <T> List<T> allResoucesInGroup(UUID groupId, Class<T> clazz) {
        return resourceRepository.fetchAllByGroupId(groupId,clazz);
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
    public Reservation reserveResource(UUID resourceId,
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
       Reservation reservationCollision = hasReservationCollision(resourceId,reservationDate);
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
        UserType type = groupsService.fetchMemberType(groupId,userId);
        Reservation r = reservationRepository.findById(reservationId).orElseThrow();
        if(type != UserType.ADMIN && !r.getUserId().equals(userId)){
            return false;
        }

        reservationRepository.deleteById(reservationId);
        return true;
    }

    public Reservation hasReservationCollision(UUID resourceId,  LocalDate reservationDate){
        Optional<Reservation> reservation = reservationRepository.findReservationByResourceIdAndDateAndApprovedTrue(resourceId,reservationDate);
        return reservation.orElse(null);
    }





}
