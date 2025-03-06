package dev.J.RepositoryForFamilies.Schedules;

import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleGlance {

        String getEventOwner();

        String getDescription();

        LocalDate getEventDate();


}
