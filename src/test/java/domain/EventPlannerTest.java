package domain;

import domain.api.EventPlanner;
import domain.fakes.FakeClock;
import domain.model.Event;
import domain.model.exceptions.UnauthorizedActionException;
import infrastructure.auth.SimpleAuthorizationProvider;
import infrastructure.persistence.InMemoryEventRepository;
import org.junit.jupiter.api.Test;
import testing.assertions.EventAssert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static testing.assertions.Assertions.assertThat;

class EventPlannerTest {

    private final InMemoryEventRepository eventRepository = new InMemoryEventRepository();
    private final SimpleAuthorizationProvider authorizationProvider = new SimpleAuthorizationProvider();
    private final EventPlanner eventPlanner = new EventService(eventRepository, authorizationProvider, new FakeClock());

    @Test
    void should_plan_a_new_event() {
        UUID organizerId = UUID.fromString("02056bba-9ca0-4fb5-8287-59ee980e8644");
        authorizationProvider.addOrganizer(organizerId);

        LocalDateTime date = LocalDateTime.of(2024, 6, 12, 19, 0);
        eventPlanner.planEvent("An event", date, 50, organizerId);

        List<Event> events = eventRepository.findAll();

        assertThat(events, EventAssert.class)
                .hasSize(1)
                .first()
                .hasName("An event")
                .isHeldAtDate(date);
    }

    @Test
    void only_organizers_are_able_to_plan_an_event() {
        LocalDateTime date = LocalDateTime.of(2024, 6, 12, 19, 0);

        assertThatExceptionOfType(UnauthorizedActionException.class)
                .isThrownBy(() -> eventPlanner.planEvent("An event", date, 50, UUID.randomUUID()))
                .withMessage("User is not authorized to perform action: plan an event");

        assertThat(eventRepository.findAll()).isEmpty();
    }

}