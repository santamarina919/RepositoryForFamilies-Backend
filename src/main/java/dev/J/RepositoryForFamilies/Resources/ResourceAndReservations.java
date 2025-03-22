package dev.J.RepositoryForFamilies.Resources;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.swing.plaf.TreeUI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
public class ResourceAndReservations {

    private Resource resource;

    private List<ReservationDTO> reservations;

    public static abstract class ReservationDTO implements ReservationAndEventPreview {

        ReservationAndEventPreview interfc;

        ReservationDTO(ReservationAndEventPreview interfc){
            this.interfc = interfc;
        }


        @Override
        public ReservationOwner getReservationOwner() {
            return interfc.getReservationOwner();
        }

        @Override
        public LocalDate getDate() {
            return interfc.getDate();
        }

        @Override
        public LocalTime getStartTime() {
            return interfc.getStartTime();
        }

        @Override
        public LocalTime getEndTime() {
            return interfc.getEndTime();
        }

        @Override
        public String getNotes() {
            return interfc.getNotes();
        }

        @Override
        public boolean getApproved() {
            return interfc.getApproved();
        }

        @Override
        public String getRejectionNote() {
            return interfc.getRejectionNote();
        }

        @Override
        public UUID getReservationId() {
            return interfc.getReservationId();
        }

        @Override
        public Event getLinkedEvent() {
            return interfc.getLinkedEvent();
        }
        public abstract boolean getWriteAccess();

        static ReservationDTO withWriteAccess(ReservationAndEventPreview interfc){
            return new ReservationDTO(interfc) {
                @Override
                public boolean getWriteAccess() {
                    return true;
                }
            };
        }

        static ReservationDTO withoutWriteAccess(ReservationAndEventPreview interfc){
            return new ReservationDTO(interfc) {
                @Override
                public boolean getWriteAccess() {
                    return false;
                }
            };
        }
    }


}

