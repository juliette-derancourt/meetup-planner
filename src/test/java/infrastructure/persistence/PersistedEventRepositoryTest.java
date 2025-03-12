package infrastructure.persistence;

import domain.model.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import testing.extensions.EventResolver;

import static testing.assertions.Assertions.assertThat;

@SpringBootTest
@ExtendWith(EventResolver.class)
class PersistedEventRepositoryTest {

    @Autowired
    private PersistedEventRepository eventRepository;

    @Test
    void should_save_then_find_event(Event event) {
        eventRepository.save(event);

        assertThat(eventRepository.findAll())
                .hasSize(1)
                .containsExactly(event);
    }

}