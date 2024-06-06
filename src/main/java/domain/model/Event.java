package domain.model;

import domain.model.exceptions.EventIsOverException;
import domain.spi.Clock;

import java.time.LocalDateTime;
import java.util.UUID;

public record Event(UUID id, String name, LocalDateTime date, Registrations registrations) {

    public static Event create(String name, LocalDateTime date, int venueCapacity) {
        if (venueCapacity <= 0) {
            throw new IllegalArgumentException("Venue capacity must be greater than zero");
        }
        return new Event(UUID.randomUUID(), name, date, Registrations.create(venueCapacity));
    }

    public void register(Attendee attendee, Clock clock) {
        if (clock.now().isAfter(date)) {
            throw new EventIsOverException();
        }
        registrations.add(attendee);
    }

    public int getNumberOfAttendees() {
        return registrations.getRegisteredAttendees();
    }

}
