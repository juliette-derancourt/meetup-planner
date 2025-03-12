package infrastructure.persistence;

import domain.model.Event;
import domain.spi.EventRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class MongoEventRepositoryTest {

    private final EventRepository eventRepository = new MongoEventRepository();

    @Test
    void should_save_then_find_event() {
        Event event = Event.create("Event", LocalDate.now(), 10);

        eventRepository.save(event);

        assertThat(eventRepository.findById(event.id()))
                .isPresent()
                .contains(event);

        assertThat(eventRepository.findAll())
                .hasSize(1)
                .containsExactly(event);
    }

}
