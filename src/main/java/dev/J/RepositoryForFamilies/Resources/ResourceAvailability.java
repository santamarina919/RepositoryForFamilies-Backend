package dev.J.RepositoryForFamilies.Resources;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.annotation.Nullable;
import lombok.Getter;

import java.time.LocalDateTime;

//Status change = when availability will flip to inverse
@Getter
public class ResourceAvailability {


    @JsonUnwrapped
    private ResourceDetails resource;

    private Availability availability;

    @Nullable
    private LocalDateTime statusChange;

    public enum Availability {
        AVAILABLE,
        UNAVAILABLE
    }

    public ResourceAvailability(Resource resource, Availability availability, LocalDateTime statusChange){
        this.availability = availability;
        this.statusChange = statusChange;

        this.resource = ResourceDetails.builder()
                .resourceId(resource.getResourceId())
                .description(resource.getDescription())
                .name(resource.getName())
                .owner(resource.getOwner())
                .type(resource.getType())
                .build();

    }


}
