package dev.J.RepositoryForFamilies.Schedules;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(EventId.class)
@Entity
public class Event {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID eventId;

    @Id
    private String userId;

    private String description;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;



}
