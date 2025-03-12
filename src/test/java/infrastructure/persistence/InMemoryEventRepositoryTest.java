package infrastructure.persistence;

import domain.spi.EventRepository;

public class InMemoryEventRepositoryTest implements EventRepositoryTest {

    @Override
    public EventRepository provideRepositoryImplementation() {
        return new InMemoryEventRepository();
    }

}
