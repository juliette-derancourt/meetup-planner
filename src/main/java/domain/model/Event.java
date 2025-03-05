package domain.model;

import domain.model.exceptions.EventIsOverException;
import domain.spi.Clock;

import java.time.LocalDate;
import java.util.UUID;

public record Event(UUID id, String name, LocalDate date, Registrations registrations) {

    public static Event create(String name, LocalDate date, int venueCapacity) {
        if (venueCapacity <= 0) {
            throw new IllegalArgumentException("Venue capacity must be greater than zero");
        }
        return new Event(UUID.randomUUID(), name, date, Registrations.create(venueCapacity));
    }

    public void register(Attendee attendee, Clock clock) {
        if (clock.today().isAfter(date)) {
            throw new EventIsOverException();
        }
        registrations.add(attendee);
    }

    public int getNumberOfAttendees() {
        return registrations.getRegisteredAttendees();
    }

}
