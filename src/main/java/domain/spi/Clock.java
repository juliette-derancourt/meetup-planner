package domain.spi;

import java.time.LocalDate;

@FunctionalInterface
public interface Clock {

    LocalDate today();

}
