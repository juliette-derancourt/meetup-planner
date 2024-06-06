package domain.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class EmailAddressTest {

    @ParameterizedTest(name = "`{0}` is valid")
    @ValueSource(strings = {
            "test@email.com",
            "hello@me.io",
            "contact@test.fr",
    })
    void should_accept_a_valid_email(String email) {
        EmailAddress emailAddress = EmailAddress.of(email);
        assertThat(emailAddress.value()).isEqualTo(email);
    }

    @ParameterizedTest(name = "throws {1} when it contains {0}")
    @MethodSource("invalidEmails")
    void should_refuse_an_invalid_email(String email, Class<?> expectedException) {
        assertThatThrownBy(() -> EmailAddress.of(email))
                .isInstanceOf(expectedException)
                .hasMessage("Invalid email address: " + email);
    }

    private static Stream<Arguments> invalidEmails() {
        return Stream.of(
                arguments(
                        named("no @", "test.email.com"),
                        named("IllegalArgument", IllegalArgumentException.class)),
                arguments(
                        named("no @ and no extension", "hello"),
                        named("IllegalArgument", IllegalArgumentException.class)),
                arguments(
                        named("no extension", "contact@test"),
                        named("IllegalArgument", IllegalArgumentException.class)),
                arguments(
                        named("no characters", "  "),
                        named("IllegalArgument", IllegalArgumentException.class))
        );
    }

}