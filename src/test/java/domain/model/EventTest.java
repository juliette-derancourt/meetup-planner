package domain.model;

import domain.fakes.FakeClock;
import domain.model.exceptions.AttendeeAlreadyRegisteredException;
import domain.model.exceptions.EventAlreadyFullException;
import domain.model.exceptions.EventIsOverException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testing.resolvers.AttendeeResolver;
import testing.resolvers.EventResolver;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static testing.assertions.Assertions.assertThat;
import static testing.resolvers.AttendeeResolver.Alice;
import static testing.resolvers.EventResolver.Full;

@ExtendWith({EventResolver.class, AttendeeResolver.class})
class EventTest {

    private final FakeClock clock = new FakeClock();

    @Test
    void should_create_an_event() {
        LocalDateTime date = LocalDateTime.of(2024, 2, 1, 20, 0);
        Event event = Event.create("Event name", date, 10);

        assertThat(event).hasAnId()
                .hasName("Event name")
                .canBeAttendedBy(10)
                .isHeldAtDate(date);
    }

    @ParameterizedTest
    @ValueSource(ints = {-10, 0})
    void should_not_create_an_event_with_no_capacity(int venueCapacity) {
        LocalDateTime date = LocalDateTime.of(2024, 2, 1, 20, 0);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Event.create("Event name", date, venueCapacity))
                .withMessage("Venue capacity must be greater than zero");
    }

    @Nested
    class FutureEvent {

        @BeforeEach
        void setUp() {
            clock.setDate(LocalDateTime.MIN);
        }

        @Test
        void should_register_an_attendee(Event event, Attendee attendee) {
            event.register(attendee, clock);

            assertThat(event).isAttendedBy(attendee);
        }

        @Test
        void should_not_register_someone_with_the_same_email_twice(Event event, @Alice Attendee alice) {
            event.register(alice, clock);

            assertThatExceptionOfType(AttendeeAlreadyRegisteredException.class)
                    .isThrownBy(() -> event.register(Attendee.withPersonalInformation("Alice 2", "alice@email.com"), clock))
                    .withMessage("Someone is already registered with this email address: alice@email.com");
        }

        @Test
        void should_not_register_someone_when_the_event_is_full(@Full Event event, Attendee attendee) {
            assertThatExceptionOfType(EventAlreadyFullException.class)
                    .isThrownBy(() -> event.register(attendee, clock));
        }

    }

    @Nested
    class PastEvent {

        @BeforeEach
        void setUp() {
            clock.setDate(LocalDateTime.MAX);
        }

        @Test
        void should_not_register_when_the_event_is_already_over(Event event, Attendee attendee) {
            assertThatExceptionOfType(EventIsOverException.class)
                    .isThrownBy(() -> event.register(attendee, clock));
        }

    }

}