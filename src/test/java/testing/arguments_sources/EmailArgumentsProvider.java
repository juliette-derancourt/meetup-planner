package testing.arguments_sources;

import net.datafaker.Faker;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class EmailArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ParameterDeclarations parameters, ExtensionContext context) {
        Faker faker = new Faker();
        return faker.stream(() -> arguments(faker.internet().emailAddress()))
                .maxLen(10)
                .generate();
    }

}
