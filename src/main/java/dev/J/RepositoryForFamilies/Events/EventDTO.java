package dev.J.RepositoryForFamilies.Events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class EventDTO {

    UUID eventId;

    String owner;

    String description;

    LocalDate date;

    LocalTime startTime;

    LocalTime endTime;

    UUID groupId;

    String name;

    public EventDTO(UUID eventId, String owner, String description, LocalDate date, LocalTime startTime, LocalTime endTime, UUID groupId, String name) {
        this.eventId = eventId;
        this.owner = owner;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.groupId = groupId;
        this.name = name;
        this.hasWriteAccess = true;
    }

    boolean hasWriteAccess;




}
