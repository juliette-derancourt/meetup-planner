package infrastructure.rest;

import domain.api.EventPlanner;
import domain.api.EventRegistrations;
import domain.api.MeetupCalendar;
import domain.model.Event;
import domain.model.exceptions.AttendeeAlreadyRegisteredException;
import domain.model.exceptions.EventAlreadyFullException;
import domain.model.exceptions.EventIsOverException;
import domain.model.exceptions.UnauthorizedActionException;
import infrastructure.rest.dto.AttendeeRequestBody;
import infrastructure.rest.dto.EventRequestBody;
import infrastructure.rest.dto.EventsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("events")
public class EventController {

    private final MeetupCalendar meetupCalendar;
    private final EventPlanner eventPlanner;
    private final EventRegistrations eventRegistrations;

    public EventController(MeetupCalendar meetupCalendar, EventPlanner eventPlanner, EventRegistrations eventRegistrations) {
        this.meetupCalendar = meetupCalendar;
        this.eventPlanner = eventPlanner;
        this.eventRegistrations = eventRegistrations;
    }

    @GetMapping
    public ResponseEntity<EventsResponse> getEvents() {
        List<Event> events = meetupCalendar.listEvents();
        return ResponseEntity.ok(EventsResponse.from(events));
    }

    @PostMapping
    public ResponseEntity<String> createEvent(@RequestBody EventRequestBody event,
                                              @RequestHeader(value = "user-id", required = false) UUID userId) {
        UUID eventId = eventPlanner.planEvent(event.name(), event.date(), event.venueCapacity(), userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventId.toString());
    }

    @PatchMapping("/{id}/register")
    public ResponseEntity<Void> registerToEvent(@PathVariable("id") UUID eventId, @RequestBody AttendeeRequestBody attendee) {
        eventRegistrations.registerTo(eventId, attendee.name(), attendee.email());
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({AttendeeAlreadyRegisteredException.class, EventIsOverException.class, EventAlreadyFullException.class})
    public ResponseEntity<Void> handle400() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<Void> handle401() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
