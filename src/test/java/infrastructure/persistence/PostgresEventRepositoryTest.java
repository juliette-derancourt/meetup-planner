package infrastructure.persistence;

import domain.model.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static testing.assertions.Assertions.assertThat;

@SpringBootTest
class PostgresEventRepositoryTest {

    @Autowired
    private PostgresEventRepository eventRepository;

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