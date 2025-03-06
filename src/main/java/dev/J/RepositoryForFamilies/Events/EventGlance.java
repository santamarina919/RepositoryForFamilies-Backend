package dev.J.RepositoryForFamilies.Events;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public interface EventGlance {

        UUID getEventId();

        String getOwner();

        String getDescription();

        LocalDate getDate();

        LocalTime getStartTime();

        LocalTime getEndTime();

        String getName();

}
