package testing.extensions;

import domain.model.Event;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.support.TypeBasedParameterResolver;

import java.time.LocalDate;

public class EventResolver extends TypeBasedParameterResolver<Event> {

    @Override
    public Event resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return Event.create(
                "A great event",
                LocalDate.of(2025, 3, 2),
                10
        );
    }

}
