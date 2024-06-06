package testing.resolvers;

import domain.model.Attendee;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.support.TypeBasedParameterResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class AttendeeResolver extends TypeBasedParameterResolver<Attendee> {

    @Override
    public Attendee resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if (parameterContext.findAnnotation(Alice.class).isPresent()) {
            return Attendee.withPersonalInformation("Alice", "alice@email.com");
        }
        if (parameterContext.findAnnotation(Bob.class).isPresent()) {
            return Attendee.withPersonalInformation("Bob", "bob@email.com");
        }
        return Attendee.withPersonalInformation("Chloé", "chloe@email.com");
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Alice {
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Bob {
    }

}
