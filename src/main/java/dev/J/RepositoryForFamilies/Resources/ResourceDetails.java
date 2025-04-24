package dev.J.RepositoryForFamilies.Resources;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Builder
public class ResourceDetails implements Serializable {

    UUID resourceId;

    String owner;

    String name;

    String description;

    String type;

    public ResourceDetails(UUID resourceId, String owner, String name, String description, String type) {
        this.resourceId = resourceId;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.type = type;
    }
}
