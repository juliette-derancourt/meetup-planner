package infrastructure.persistence;

import domain.model.Event;
import domain.spi.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import testing.extensions.EventResolver;

import static testing.assertions.Assertions.assertThat;

@ExtendWith(EventResolver.class)
public interface EventRepositoryTest {

    EventRepository provideRepositoryImplementation();

    @Test
    default void should_save_then_find_an_event(Event event) {
        EventRepository eventRepository = provideRepositoryImplementation();

        eventRepository.save(event);

         assertThat(eventRepository.findAll())
                .hasSize(1)
                .containsExactly(event);
    }

}
