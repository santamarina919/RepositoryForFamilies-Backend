package dev.J.RepositoryForFamilies.Resources;

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
@Entity
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID reservationId;

    private UUID resourceId;

    private String userId;

    private UUID groupId;

    private UUID linkedEvent;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private String notes;

    private boolean approved;

    private String rejectionNote;


}
