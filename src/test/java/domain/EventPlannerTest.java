package domain;

import domain.api.EventPlanner;
import domain.fakes.FakeClock;
import domain.model.Event;
import domain.model.exceptions.UnauthorizedActionException;
import infrastructure.auth.SimpleAuthorizationProvider;
import infrastructure.persistence.InMemoryEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import testing.extensions.UuidResolver;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static testing.assertions.Assertions.assertThat;
import static testing.assertions.EventAssert.EVENT;

@ExtendWith(UuidResolver.class)
class EventPlannerTest {

    private final InMemoryEventRepository eventRepository = new InMemoryEventRepository();
    private final SimpleAuthorizationProvider authorizationProvider = new SimpleAuthorizationProvider();
    private final EventPlanner eventPlanner = new EventService(eventRepository, authorizationProvider, new FakeClock());

    @Test
    void should_plan_a_new_event(UUID organizerId) {
        authorizationProvider.addOrganizer(organizerId);

        LocalDate date = LocalDate.of(2025, 6, 12);
        eventPlanner.planEvent("An event", date, 50, organizerId);

        List<Event> events = eventRepository.findAll();

        assertThat(events)
                .hasSize(1)
                .first(EVENT)
                .hasName("An event")
                .isHeldAtDate(date);
    }

    @Test
    void only_organizers_are_able_to_plan_an_event() {
        LocalDate date = LocalDate.of(2025, 6, 12);

        assertThatExceptionOfType(UnauthorizedActionException.class)
                .isThrownBy(() -> eventPlanner.planEvent("An event", date, 50, UUID.randomUUID()))
                .withMessage("User is not authorized to perform action: plan an event");

        assertThat(eventRepository.findAll()).isEmpty();
    }

}