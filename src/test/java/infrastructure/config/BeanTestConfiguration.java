package infrastructure.config;

import domain.fakes.FakeClock;
import domain.spi.Clock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class BeanTestConfiguration {

    @Bean
    Clock fakeClock() {
        return new FakeClock();
    }

}
