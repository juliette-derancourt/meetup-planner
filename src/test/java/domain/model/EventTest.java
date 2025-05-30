package domain.model;

import domain.fakes.FakeClock;
import domain.model.exceptions.EventAlreadyFullException;
import domain.model.exceptions.EventIsOverException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testing.extensions.AttendeeResolver;
import testing.extensions.DateResolver;
import testing.extensions.EventResolver;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static testing.assertions.Assertions.assertThat;
import static testing.extensions.EventResolver.Full;

@ExtendWith({EventResolver.class, AttendeeResolver.class, DateResolver.class})
class EventTest {

    private final FakeClock clock = new FakeClock();

    @Test
    void should_create_an_event(LocalDate date) {
        Event event = Event.create("Event name", date, 10);

        assertThat(event).hasAnId()
                .hasName("Event name")
                .canBeAttendedBy(10)
                .isHeldAtDate(date);
    }

    @ParameterizedTest
    @ValueSource(ints = {-10, 0})
    void should_not_create_an_event_with_no_capacity(int venueCapacity, LocalDate date) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Event.create("Event name", date, venueCapacity))
                .withMessage("Venue capacity must be greater than zero");
    }

    @Test
    void should_register_an_attendee(Event event, Attendee attendee) {
        event.register(attendee, clock);

        assertThat(event).isAttendedBy(attendee);
    }

    @Test
    void should_not_register_someone_when_the_event_is_full(@Full Event event, Attendee attendee) {
        assertThatExceptionOfType(EventAlreadyFullException.class)
                .isThrownBy(() -> event.register(attendee, clock));
    }

    @Test
    void should_not_register_when_the_event_is_already_over(Event event, Attendee attendee) {
        clock.setDate(event.date().plusDays(1));

        assertThatExceptionOfType(EventIsOverException.class)
                .isThrownBy(() -> event.register(attendee, clock));
    }

}