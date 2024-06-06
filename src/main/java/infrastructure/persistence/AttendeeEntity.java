package infrastructure.persistence;

import domain.model.Attendee;
import domain.model.EmailAddress;
import jakarta.persistence.Embeddable;

import java.util.List;

@Embeddable
public class AttendeeEntity {

    private String name;

    private String email;

    public AttendeeEntity() {
    }

    private AttendeeEntity(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public static List<AttendeeEntity> from(List<Attendee> attendees) {
        return attendees.stream().map(AttendeeEntity::from).toList();
    }

    public static AttendeeEntity from(Attendee attendee) {
        return new AttendeeEntity(attendee.name(), attendee.email().value());
    }

    public Attendee toDomainObject() {
        return new Attendee(name, EmailAddress.of(email));
    }

}
