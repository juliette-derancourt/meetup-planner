package domain.fakes;

import domain.spi.Clock;

import java.time.LocalDate;

public class FakeClock implements Clock {

    private LocalDate date = LocalDate.MIN;

    @Override
    public LocalDate today() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
