package dev.J.RepositoryForFamilies.Resources;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class ResourceReservationDetails {
    @JsonUnwrapped
    private final ResourceDetails resource;

    private final Boolean approval;

    private final String rejectionNote;

    public ResourceReservationDetails(ResourceDetails resource, Boolean approval, String rejectionNote){
        this.resource = resource;
        this.approval = approval;
        this.rejectionNote = rejectionNote;

    }
}
