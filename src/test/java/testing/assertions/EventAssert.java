package testing.assertions;

import domain.model.Attendee;
import domain.model.Event;
import org.assertj.core.api.AbstractAssert;

import java.time.LocalDate;

public class EventAssert extends AbstractAssert<EventAssert, Event> {

    public EventAssert(Event event) {
        super(event, EventAssert.class);
    }

    public EventAssert hasAnId() {
        isNotNull();
        if (actual.id() == null) {
            failWithMessage("Expected id to be present");
        }
        return this;
    }

    public EventAssert hasName(String name) {
        isNotNull();
        if (!actual.name().equals(name)) {
            failWithMessage("Expected name to be <%s> but was <%s>", name, actual.name());
        }
        return this;
    }

    public EventAssert isHeldAtDate(LocalDate date) {
        isNotNull();
        if (!actual.date().equals(date)) {
            failWithMessage("Expected date to be <%s> but was <%s>", date, actual.date());
        }
        return this;
    }

    public EventAssert isAttendedBy(Attendee attendee) {
        isNotNull();
        if (!actual.registrations().isRegistered(attendee)) {
            failWithMessage("Expected <%s> to attend event", attendee.name());
        }
        return this;
    }

    public EventAssert isAttendedBy(String name, String email) {
        isNotNull();
        Attendee expectedAttendee = Attendee.withPersonalInformation(name, email);
        if (!actual.registrations().isRegistered(expectedAttendee)) {
            failWithMessage("Expected <%s> to be registered but was not", expectedAttendee);
        }
        return this;
    }

    public EventAssert hasAttendees(int count) {
        isNotNull();
        if (actual.getNumberOfAttendees() != count) {
            failWithMessage("Expected number of attendees to be <%s> but was <%s>", count, actual.getNumberOfAttendees());
        }
        return this;
    }

    public EventAssert canBeAttendedBy(int numberOfPeople) {
        isNotNull();
        int actualCapacity = actual.registrations().venueCapacity();
        if (actualCapacity != numberOfPeople) {
            failWithMessage("Expected venue capacity to be <%s> but was <%s>", numberOfPeople, actualCapacity);
        }
        return this;
    }

}
