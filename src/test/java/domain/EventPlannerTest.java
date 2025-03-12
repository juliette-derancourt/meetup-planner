package domain;

import domain.api.EventPlanner;
import domain.fakes.FakeClock;
import domain.model.Event;
import infrastructure.auth.SimpleAuthorizationProvider;
import infrastructure.persistence.InMemoryEventRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static testing.assertions.Assertions.assertThat;

class EventPlannerTest {

    private final InMemoryEventRepository eventRepository = new InMemoryEventRepository();
    private final SimpleAuthorizationProvider authorizationProvider = new SimpleAuthorizationProvider();
    private final EventPlanner eventPlanner = new EventService(eventRepository, authorizationProvider, new FakeClock());

    @Test
    void should_plan_a_new_event() {
        UUID organizerId = UUID.fromString("02056bba-9ca0-4fb5-8287-59ee980e8644");
        authorizationProvider.addOrganizer(organizerId);

        LocalDate date = LocalDate.of(2025, 6, 12);
        eventPlanner.planEvent("An event", date, 50, organizerId);

        List<Event> events = eventRepository.findAll();

        Event event = events.get(0);

        assertThat(event)
                .hasName("An event")
                .isHeldAtDate(date);
    }

}