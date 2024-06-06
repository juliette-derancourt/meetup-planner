package infrastructure.persistence;

import domain.model.Event;
import domain.spi.EventRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class PersistedEventRepository implements EventRepository {

    private final JpaEventRepository repository;

    public PersistedEventRepository(JpaEventRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Event event) {
        repository.save(EventEntity.from(event));
    }

    @Override
    public List<Event> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(EventEntity::toDomainObject)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Event> findById(UUID id) {
        return repository.findById(id).map(EventEntity::toDomainObject);
    }

}