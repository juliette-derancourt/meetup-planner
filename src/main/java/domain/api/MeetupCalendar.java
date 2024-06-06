package domain.api;

import domain.model.Event;

import java.util.List;

@FunctionalInterface
public interface MeetupCalendar {

    List<Event> listEvents();

}
