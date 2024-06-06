package domain.model;

public record Attendee(String name, EmailAddress email) {

    public Attendee {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Attendee name cannot be empty");
        }
    }

    public static Attendee withPersonalInformation(String name, String email) {
        return new Attendee(name, EmailAddress.of(email));
    }

}
