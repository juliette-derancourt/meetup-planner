package infrastructure.persistence;

import domain.model.Event;
import domain.spi.EventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import testing.extensions.EventResolver;

import static testing.assertions.Assertions.assertThat;

@ExtendWith(EventResolver.class)
public interface EventRepositoryTest {

    EventRepository buildRepository();

    @Test
    @DisplayName("Should save then find an event")
    default void should_save_then_find_event(Event event) {
        EventRepository eventRepository = buildRepository();
        eventRepository.save(event);

        assertThat(eventRepository.findById(event.id()))
                .isPresent()
                .contains(event);

        assertThat(eventRepository.findAll())
                .hasSize(1)
                .containsExactly(event);
    }

}
