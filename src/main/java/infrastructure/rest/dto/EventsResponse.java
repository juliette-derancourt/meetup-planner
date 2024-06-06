package infrastructure.rest.dto;

import domain.model.Event;

import java.util.List;

public record EventsResponse(List<EventResponse> events) {

    public static EventsResponse from(List<Event> events) {
        return new EventsResponse(events.stream().map(EventResponse::from).toList());
    }

}
