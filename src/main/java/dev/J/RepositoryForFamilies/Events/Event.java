package dev.J.RepositoryForFamilies.Events;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
public class Event implements Comparable<Event>, Serializable {
    @Column(name = "event_id")
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID eventId;

    @Column(name = "group_id")
    private UUID groupId;

    @Column(name = "owner")
    @Nonnull
    private String owner;

    @Column(name = "description")
    @Nullable
    private String description;

    @Column(name = "date")
    @Nonnull
    private LocalDate date;

    //If the start time is not null then end time cannot be null
    @Column(name = "start_time")
    @Nullable
    private LocalTime startTime;

    @Column(name = "end_time")
    @Nullable
    private LocalTime endTime;

    @Column(name = "name")
    @Nonnull
    private String name;


    @Override
    public int compareTo(Event o) {
        if(this.date.isBefore(o.getDate())){
            return -1;
        }
        if(this.date.isAfter(o.getDate())){
            return 1;
        }

        //Events with start time last the whole day and thus start at 12:00am to 11:59pm so they are always less then everything else
        if(startTime == null && o.getStartTime() == null){
            return 0;
        }
        else if(startTime == null){
            return -1;
        }
        else if(o.getStartTime() == null){
            return 1;
        }


        if(startTime.isBefore(o.getStartTime())){
            return -1;
        }
        else{
            return 1;
        }

    }


    public interface Details {
        UUID getEventId();

        String getName();

        LocalDate getDate();

        LocalTime getStartTime();

        LocalTime getEndTime();

        String getDescription();

        String getOwner();
    }

}
