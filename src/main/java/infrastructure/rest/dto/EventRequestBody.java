package infrastructure.rest.dto;

import java.time.LocalDateTime;

public record EventRequestBody(String name, LocalDateTime date, int venueCapacity) {
}
