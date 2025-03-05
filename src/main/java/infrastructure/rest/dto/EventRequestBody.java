package infrastructure.rest.dto;

import java.time.LocalDate;

public record EventRequestBody(String name, LocalDate date, int venueCapacity) {

}
