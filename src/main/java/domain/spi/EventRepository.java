package domain.spi;

import domain.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository {

    void save(Event event);

    List<Event> findAll();

    Optional<Event> findById(UUID id);

}
