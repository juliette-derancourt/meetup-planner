package infrastructure.rest.dto;

import domain.model.Event;

import java.time.LocalDate;

public record EventResponse(String name, LocalDate date, int attendees) {

    static EventResponse from(Event event) {
        return new EventResponse(event.name(), event.date(), event.getNumberOfAttendees());
    }

}
