package domain.api;

import java.util.UUID;

public interface EventRegistrations {
    void registerTo(UUID eventId, String name, String email);
}
