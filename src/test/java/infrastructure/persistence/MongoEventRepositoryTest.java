package infrastructure.persistence;

import domain.spi.EventRepository;

public class MongoEventRepositoryTest implements EventRepositoryTest {

    @Override
    public EventRepository provideRepositoryImplementation() {
        return new MongoEventRepository();
    }

}
