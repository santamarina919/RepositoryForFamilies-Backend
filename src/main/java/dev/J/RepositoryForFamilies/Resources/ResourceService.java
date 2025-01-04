package dev.J.RepositoryForFamilies.Resources;

import dev.J.RepositoryForFamilies.Groups.GroupsService;
import dev.J.RepositoryForFamilies.Groups.UserType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ResourceService
{

    private final ResourceRepository resourceRepository;

    private final ReservationRepository reservationRepository;

    private final GroupsService groupsService;

    public List<Resource> allResources(String ownerId){
        return resourceRepository.findAllByOwner(ownerId);
    }

    @Transactional
    public void createResource(String owner, String name, String description){
        UUID resourceId = UUID.randomUUID();
        Resource r = new Resource(resourceId, owner, name, description);
        resourceRepository.save(r);
    }


    @Transactional
    public boolean deleteResource(UUID groupId, String userId, UUID resourceId){
        UserType type = groupsService.fetchMemberType(groupId,userId);
        Resource resource = resourceRepository.findResourceByResourceId(resourceId);
        if(type != UserType.ADMIN && !resource.getOwner().equals(userId)){
            return false;
        }

        resourceRepository.deleteById(resourceId);


        return true;
    }

    @Transactional
    public boolean approveReservation(UUID reservationId, UUID resourceId, String userId){
        Resource resource = resourceRepository.findResourceByResourceId(resourceId);
        if(!resource.getOwner().equals(userId)){
            return false;
        }

        resourceRepository.approveReservation(reservationId);
        return true;
    }

    @Transactional
    public boolean denyReservation(UUID reservationId, UUID resourceId, String userId, String reason){
        Resource resource = resourceRepository.findResourceByResourceId(resourceId);
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
                                   String notes){
        Reservation r = new Reservation(UUID.randomUUID(),resourceId,userId,reservationDate,startTime,endTime,notes,true);
        reservationRepository.save(r);
    }


    @Transactional
    public boolean reserveResource(UUID resourceId,
                                   String userId,
                                   LocalDate reservationDate,
                                   LocalTime startTime,
                                   LocalTime endTime,
                                   String notes){
       Resource resource = resourceRepository.findResourceByResourceId(resourceId);
       if(resource.getOwner().equals(userId)){
           return false;
       }

       if(notes == null) {
           notes = "Reserved by resource owner for this period of time";
       }
       Reservation r = new Reservation(UUID.randomUUID(),resourceId,userId,reservationDate,startTime,endTime,notes,true);
       reservationRepository.save(r);

       return true;
    }

    @Transactional
    public boolean deleteReservation(UUID reservationId, UUID resourceId,UUID groupId, String userId){
        UserType type = groupsService.fetchMemberType(groupId,userId);
        Reservation r = reservationRepository.findById(reservationId).orElseThrow();
        if(type != UserType.ADMIN && !r.getUserId().equals(userId)){
            return false;
        }

        resourceRepository.deleteById(reservationId);
        return true;
    }






}
