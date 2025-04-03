package dev.J.RepositoryForFamilies.Events;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
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
public class Event implements Comparable<Event> {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID eventId;

    @Id
    private UUID groupId;

    @Nonnull
    private String owner;

    @Nullable
    private String description;

    @Nonnull
    private LocalDate date;

    //If the start time is not null then end time cannot be null
    @Nullable
    private LocalTime startTime;

    @Nullable
    private LocalTime endTime;

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
}
