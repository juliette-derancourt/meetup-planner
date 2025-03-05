package domain.api;

import java.time.LocalDate;
import java.util.UUID;

@FunctionalInterface
public interface EventPlanner {

    UUID planEvent(String name, LocalDate date, int venueCapacity, UUID organizerId);

}
