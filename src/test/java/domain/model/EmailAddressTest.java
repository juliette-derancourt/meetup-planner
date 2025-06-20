package domain.model;

import domain.model.exceptions.InvalidEmailFormatException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import testing.arguments_sources.EmailArgumentsProvider;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class EmailAddressTest {

    @ParameterizedTest(name = "`{0}` is valid")
    @ArgumentsSource(EmailArgumentsProvider.class)
    void should_accept_a_valid_email(String email) {
        EmailAddress emailAddress = EmailAddress.of(email);
        assertThat(emailAddress.value()).isEqualTo(email);
    }

    @ParameterizedTest(name = "throws {1} when it contains {0}")
    @MethodSource("invalidEmails")
    void should_refuse_an_invalid_email(String email, Class<?> expectedException, String message) {
        assertThatThrownBy(() -> EmailAddress.of(email))
                .isInstanceOf(expectedException)
                .hasMessage(message);
    }

    private static Stream<Arguments> invalidEmails() {
        return Stream.of(
                arguments(
                        named("no @", "test.email.com"),
                        named("InvalidEmailFormat", InvalidEmailFormatException.class),
                        "Invalid email address: test.email.com"
                ),
                arguments(
                        named("no @ and no extension", "hello"),
                        named("InvalidEmailFormat", InvalidEmailFormatException.class),
                        "Invalid email address: hello"
                ),
                arguments(
                        named("no extension", "contact@test"),
                        named("InvalidEmailFormat", InvalidEmailFormatException.class),
                        "Invalid email address: contact@test"
                ),
                arguments(
                        named("no characters", "  "),
                        named("IllegalArgument", IllegalArgumentException.class),
                        "Email is null or empty"
                )
        );
    }

}