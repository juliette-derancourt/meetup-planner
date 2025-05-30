package testing.extensions;

import domain.model.Attendee;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.support.TypeBasedParameterResolver;

public class AttendeeResolver extends TypeBasedParameterResolver<Attendee> {

    @Override
    public Attendee resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return Attendee.withPersonalInformation("Alice", "alice@email.com");
    }

}
