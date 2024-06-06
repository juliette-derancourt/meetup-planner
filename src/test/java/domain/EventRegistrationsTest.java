package domain;

import domain.api.EventRegistrations;
import domain.fakes.FakeClock;
import domain.model.Event;
import infrastructure.auth.SimpleAuthorizationProvider;
import infrastructure.persistence.InMemoryEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import testing.resolvers.EventResolver;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static testing.assertions.Assertions.assertThat;

@ExtendWith(EventResolver.class)
class EventRegistrationsTest {

    private final InMemoryEventRepository eventRepository = new InMemoryEventRepository();
    private final SimpleAuthorizationProvider authorizationProvider = new SimpleAuthorizationProvider();
    private final EventRegistrations eventRegistrations = new EventService(eventRepository, authorizationProvider, new FakeClock());

    @Test
    void should_register_to_an_event(Event event) {
        UUID eventId = event.id();
        eventRepository.save(event);

        eventRegistrations.registerTo(eventId, "Donna", "donna@email.com");

        Event eventAfterRegistration = eventRepository.findById(eventId).orElseThrow();
        assertThat(eventAfterRegistration)
                .hasAttendees(1)
                .isAttendedBy("Donna", "donna@email.com");
    }

    @Test
    void should_not_register_to_an_unknown_event() {
        UUID eventId = UUID.randomUUID();

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> eventRegistrations.registerTo(eventId, "Donna", "donna@email.com"))
                .withMessageMatching("Event with id .+ not found");
    }

}