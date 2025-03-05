package infrastructure.persistence;

import domain.model.Attendee;
import domain.model.Event;
import domain.model.Registrations;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class EventEntity {

    @Id
    private UUID id;

    private String name;

    private LocalDate date;

    private int venueCapacity;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<AttendeeEntity> attendees;

    public EventEntity() {
    }

    private EventEntity(UUID id, String name, LocalDate date, int venueCapacity, List<AttendeeEntity> attendees) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.venueCapacity = venueCapacity;
        this.attendees = attendees;
    }

    public static EventEntity from(Event event) {
        return new EventEntity(event.id(), event.name(), event.date(), event.registrations().venueCapacity(), AttendeeEntity.from(event.registrations().attendees()));
    }

    public Event toDomainObject() {
        List<Attendee> domainAttendees = new ArrayList<>(attendees.stream().map(AttendeeEntity::toDomainObject).toList());
        return new Event(id, name, date, new Registrations(venueCapacity, domainAttendees));
    }

}
