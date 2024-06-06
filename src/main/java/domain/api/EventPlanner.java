package domain.api;

import java.time.LocalDateTime;
import java.util.UUID;

@FunctionalInterface
public interface EventPlanner {

    UUID planEvent(String name, LocalDateTime date, int venueCapacity, UUID organizerId);

}
