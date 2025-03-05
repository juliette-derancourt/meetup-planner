package infrastructure.config;

import domain.EventService;
import domain.spi.Clock;
import domain.spi.EventRepository;
import domain.spi.UserAuthorizationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class BeanConfiguration {

    @Bean
    EventService eventService(EventRepository eventRepository, UserAuthorizationProvider authorizationProvider, Clock clock) {
        return new EventService(eventRepository, authorizationProvider, clock);
    }

    @Bean
    Clock clock() {
        return LocalDate::now;
    }

}
