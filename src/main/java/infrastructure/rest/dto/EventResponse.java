package infrastructure.rest.dto;

import domain.model.Event;

import java.time.LocalDateTime;

public record EventResponse(String name, LocalDateTime date, int attendees) {

    static EventResponse from(Event event) {
        return new EventResponse(event.name(), event.date(), event.getNumberOfAttendees());
    }

}
