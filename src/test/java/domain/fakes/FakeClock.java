package domain.fakes;

import domain.spi.Clock;

import java.time.LocalDateTime;

public class FakeClock implements Clock {

    private LocalDateTime date = LocalDateTime.MIN;

    @Override
    public LocalDateTime now() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
