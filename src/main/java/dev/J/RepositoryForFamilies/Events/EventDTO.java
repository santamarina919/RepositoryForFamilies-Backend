package dev.J.RepositoryForFamilies.Events;

import dev.J.RepositoryForFamilies.Resources.ResourceReservationDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class EventDTO {

    private UUID eventId;

    private String owner;

    private String description;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private UUID groupId;

    private String name;

    public EventDTO(UUID eventId, String owner, String description, LocalDate date, LocalTime startTime, LocalTime endTime, UUID groupId, String name) {
        this.eventId = eventId;
        this.owner = owner;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.groupId = groupId;
        this.name = name;
        this.hasWriteAccess = false;
        this.reservedResources = new ArrayList<>();
    }

    private boolean hasWriteAccess;

    private List<ResourceReservationDetails> reservedResources;


}
