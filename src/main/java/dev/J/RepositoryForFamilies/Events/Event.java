package dev.J.RepositoryForFamilies.Events;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@IdClass(EventId.class)
@Entity
@SqlResultSetMapping(
        name = "event-dto", classes = {
        @ConstructorResult(
                targetClass = EventDTO.class,
                columns = {
                        @ColumnResult(name = "event_id", type = UUID.class),
                        @ColumnResult(name = "owner", type = String.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "date", type = LocalDate.class),
                        @ColumnResult(name = "start_time", type = LocalTime.class),
                        @ColumnResult(name = "end_time", type = LocalTime.class),
                        @ColumnResult(name = "group_id", type = UUID.class),
                        @ColumnResult(name = "name", type = String.class),



                }
        )
        }
)
public class Event {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID eventId;

    @Id
    private UUID groupId;

    private String owner;

    private String description;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private String name;



}
