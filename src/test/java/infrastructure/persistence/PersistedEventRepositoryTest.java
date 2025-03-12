package infrastructure.persistence;

import domain.spi.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PersistedEventRepositoryTest implements EventRepositoryTest {

    @Autowired
    private PersistedEventRepository eventRepository;

    @Override
    public EventRepository provideRepositoryImplementation() {
        return eventRepository;
    }

}