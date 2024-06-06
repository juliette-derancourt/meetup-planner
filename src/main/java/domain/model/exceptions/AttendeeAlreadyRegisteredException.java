package domain.model.exceptions;

import domain.model.Attendee;

public class AttendeeAlreadyRegisteredException extends RuntimeException {

    public AttendeeAlreadyRegisteredException(Attendee attendee) {
        super("Someone is already registered with this email address: " + attendee.email());
    }

}
