package domain.model;

import domain.fakes.FakeClock;
import domain.model.exceptions.EventAlreadyFullException;
import domain.model.exceptions.EventIsOverException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static testing.assertions.Assertions.assertThat;

class EventTest {

    private final FakeClock clock = new FakeClock();

    @Test
    void should_create_an_event() {
        LocalDate date = LocalDate.of(2025, 2, 1);
        Event event = Event.create("Event name", date, 10);

        assertThat(event).hasAnId()
                .hasName("Event name")
                .canBeAttendedBy(10)
                .isHeldAtDate(date);
    }

    @Test
    void should_not_create_an_event_with_no_capacity() {
        LocalDate date = LocalDate.of(2025, 2, 1);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> Event.create("Event name", date, 0))
                .withMessage("Venue capacity must be greater than zero");
    }

    @Test
    void should_not_create_an_event_with_negative_capacity() {
        LocalDate date = LocalDate.of(2025, 2, 1);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> Event.create("Event name", date, -10))
                .withMessage("Venue capacity must be greater than zero");
    }

    @Test
    void should_register_an_attendee() {
        Event event = new Event(
                UUID.fromString("182754a6-939b-41a2-a610-2470c01d755b"),
                "An event",
                LocalDate.of(2025, 2, 2),
                new Registrations(
                        2,
                        new ArrayList<>()
                ));

        Attendee newAttendee = Attendee.withPersonalInformation("An attendee", "test@email.com");
        event.register(newAttendee, clock);

        assertThat(event).isAttendedBy(newAttendee);
    }

    @Test
    void should_not_register_someone_when_the_event_is_full() {
        Event event = new Event(
                UUID.fromString("182754a6-939b-41a2-a610-2470c01d755b"),
                "An event",
                LocalDate.of(2025, 2, 2),
                new Registrations(
                        2,
                        List.of(
                                Attendee.withPersonalInformation("Attendee #1", "first@email.com"),
                                Attendee.withPersonalInformation("Attendee #2", "second@email.com")
                        )
                ));

        Attendee newAttendee = Attendee.withPersonalInformation("Attendee #3", "third@email.com");

        assertThatExceptionOfType(EventAlreadyFullException.class)
                .isThrownBy(() -> event.register(newAttendee, clock));
    }

    @Test
    void should_not_register_when_the_event_is_already_over() {
        clock.setDate(LocalDate.MAX);

        Event event = new Event(
                UUID.fromString("182754a6-939b-41a2-a610-2470c01d755b"),
                "An event",
                LocalDate.of(2025, 2, 2),
                new Registrations(
                        10,
                        List.of(
                                Attendee.withPersonalInformation("Attendee #1", "first@email.com")
                        )
                ));

        Attendee newAttendee = Attendee.withPersonalInformation("Attendee #2", "second@email.com");

        assertThatExceptionOfType(EventIsOverException.class)
                .isThrownBy(() -> event.register(newAttendee, clock));
    }

}