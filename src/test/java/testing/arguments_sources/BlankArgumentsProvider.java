package testing.arguments_sources;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class BlankArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<Arguments> provideArguments(ParameterDeclarations parameters, ExtensionContext context) {
        return Stream.of(
                arguments(""),
                arguments(" "),
                arguments("\t")
        );
    }

}
