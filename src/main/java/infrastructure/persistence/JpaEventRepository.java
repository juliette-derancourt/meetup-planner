package infrastructure.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface JpaEventRepository extends CrudRepository<EventEntity, UUID> {
}
