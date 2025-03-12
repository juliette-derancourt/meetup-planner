package testing.arguments_sources;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;

public class BlankArgumentsSource implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                argumentSet("empty", ""),
                argumentSet("one space", " "),
                argumentSet("one tab", "\t")
        );
    }

}
