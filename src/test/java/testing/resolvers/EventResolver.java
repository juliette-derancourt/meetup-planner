package testing.resolvers;

import domain.model.Attendee;
import domain.model.Event;
import domain.model.Registrations;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.support.TypeBasedParameterResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class EventResolver extends TypeBasedParameterResolver<Event> {

    @Override
    public Event resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if (parameterContext.findAnnotation(Full.class).isPresent()) {
            return buildFullEvent();
        }

        return Event.create(
                "A great event",
                LocalDateTime.of(2024, 3, 2, 12, 0),
                10
        );
    }

    private static Event buildFullEvent() {
        return new Event(
                UUID.fromString("182754a6-939b-41a2-a610-2470c01d755b"),
                "A full event",
                LocalDateTime.of(2024, 2, 2, 12, 0),
                new Registrations(
                        2,
                        List.of(
                                Attendee.withPersonalInformation("Attendee #1", "first@email.com"),
                                Attendee.withPersonalInformation("Attendee #2", "second@email.com")
                        )
                ));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Full {
    }

}
