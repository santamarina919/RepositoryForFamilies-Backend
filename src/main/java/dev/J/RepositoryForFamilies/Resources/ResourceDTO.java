package dev.J.RepositoryForFamilies.Resources;

import java.util.UUID;

public interface ResourceDTO {

    UUID getResourceId();

    String getOwner();

    String getName();

    String getDescription();

    String getType();
}
