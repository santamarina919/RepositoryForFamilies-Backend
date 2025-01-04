package dev.J.RepositoryForFamilies.Groups;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;



public interface GroupCardDTO {
    UUID getId();

    String getName();

    String getType();
}
