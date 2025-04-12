package dev.J.RepositoryForFamilies.Resources;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import dev.J.RepositoryForFamilies.Lists.ListsController;

public class ResourceReservationDetails {
    @JsonUnwrapped
    private final Resource.Details resource;

    private final Boolean approval;

    private final String rejectionNote;

    public ResourceReservationDetails(Resource.Details resource, Boolean approval, String rejectionNote){
        this.resource = resource;
        this.approval = approval;
        this.rejectionNote = rejectionNote;

    }
}
