package infrastructure.persistence;

import domain.spi.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PersistedEventRepositoryTest implements EventRepositoryTest {

    @Autowired
    private JpaEventRepository jpaRepository;

    @Override
    public EventRepository buildRepository() {
        return new PersistedEventRepository(jpaRepository);
    }

}