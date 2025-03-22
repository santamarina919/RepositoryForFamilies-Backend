package dev.J.RepositoryForFamilies.Resources;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;


public interface ReservationAndEventPreview {
    ReservationOwner getReservationOwner();

    public static interface ReservationOwner {
        String getEmail();
    }

    LocalDate getDate();

    LocalTime getStartTime();

    LocalTime getEndTime();

    String getNotes();

    boolean getApproved();

    String getRejectionNote();

    UUID getReservationId();

    Event getLinkedEvent();

    interface Event {
        UUID getEventId();

        String getName();
    }
}
