package testing.assertions;

import domain.model.Event;

public class Assertions extends org.assertj.core.api.Assertions {

    public static EventAssert assertThat(Event event) {
        return new EventAssert(event);
    }

    /*

    public static AttendeeAssert assertThat(Attendee attendee) {
        return new AttendeeAssert(attendee);
    }

    ...

    */

}
