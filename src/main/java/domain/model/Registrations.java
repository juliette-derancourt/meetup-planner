package domain.model;

import domain.model.exceptions.AttendeeAlreadyRegisteredException;
import domain.model.exceptions.EventAlreadyFullException;

import java.util.ArrayList;
import java.util.List;

public record Registrations(int venueCapacity, List<Attendee> attendees) {

    public static Registrations create(int venueCapacity) {
        return new Registrations(venueCapacity, new ArrayList<>());
    }

    public void add(Attendee attendee) {
        if (isFull()) {
            throw new EventAlreadyFullException();
        }
        if (isRegistered(attendee)) {
            throw new AttendeeAlreadyRegisteredException(attendee);
        }
        attendees.add(attendee);
    }

    public boolean isRegistered(Attendee attendee) {
        return attendees.stream().anyMatch(a -> a.email().equals(attendee.email()));
    }

    private boolean isFull() {
        return attendees.size() == venueCapacity;
    }

    public int getRegisteredAttendees() {
        return attendees.size();
    }

}
