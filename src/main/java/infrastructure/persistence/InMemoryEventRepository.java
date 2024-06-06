package infrastructure.persistence;

import domain.model.Event;
import domain.spi.EventRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryEventRepository implements EventRepository {

    private final Map<UUID, Event> events = new HashMap<>();

    @Override
    public void save(Event event) {
        events.put(event.id(), event);
    }

    @Override
    public List<Event> findAll() {
        return List.copyOf(events.values());
    }

    @Override
    public Optional<Event> findById(UUID id) {
        return Optional.ofNullable(events.get(id));
    }

}
