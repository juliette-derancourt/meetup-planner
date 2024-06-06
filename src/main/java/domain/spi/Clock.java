package domain.spi;

import java.time.LocalDateTime;

@FunctionalInterface
public interface Clock {

    LocalDateTime now();

}
