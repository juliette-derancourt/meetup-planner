package domain;

import domain.api.EventPlanner;
import domain.api.EventRegistrations;
import domain.api.MeetupCalendar;
import domain.model.Attendee;
import domain.model.Event;
import domain.model.exceptions.UnauthorizedActionException;
import domain.spi.Clock;
import domain.spi.EventRepository;
import domain.spi.UserAuthorizationProvider;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class EventService implements MeetupCalendar, EventPlanner, EventRegistrations {

    private final EventRepository eventRepository;
    private final UserAuthorizationProvider authorizationProvider;
    private final Clock clock;

    public EventService(EventRepository eventRepository, UserAuthorizationProvider authorizationProvider, Clock clock) {
        this.eventRepository = eventRepository;
        this.authorizationProvider = authorizationProvider;
        this.clock = clock;
    }

    @Override
    public List<Event> listEvents() {
        return eventRepository.findAll();
    }

    @Override
    public UUID planEvent(String name, LocalDate date, int venueCapacity, UUID userId) {
        if (!authorizationProvider.isOrganizer(userId)) {
            throw new UnauthorizedActionException("plan an event");
        }

        Event event = Event.create(name, date, venueCapacity);
        eventRepository.save(event);

        return event.id();
    }

    @Override
    public void registerTo(UUID eventId, String name, String email) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException("Event with id " + eventId + " not found"));

        Attendee attendee = Attendee.withPersonalInformation(name, email);

        event.register(attendee, clock);
        eventRepository.save(event);
    }

}
