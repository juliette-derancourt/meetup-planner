package domain.model;

import domain.model.exceptions.InvalidEmailFormatException;
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

    @ParameterizedTest
    @ValueSource(strings = {
            "test@email.com",
            "hello@me.io",
            "contact@test.fr",
    })
    void should_accept_a_valid_email(String email) {
        EmailAddress emailAddress = EmailAddress.of(email);
        assertThat(emailAddress.value()).isEqualTo(email);
    }

    @ParameterizedTest
    @MethodSource("invalidEmails")
    void should_refuse_an_invalid_email(String email, Class<?> expectedException) {
        assertThatThrownBy(() -> EmailAddress.of(email))
                .isInstanceOf(expectedException);
    }

    private static Stream<Arguments> invalidEmails() {
        return Stream.of(
                arguments("test.email.com", InvalidEmailFormatException.class),
                arguments("hello", InvalidEmailFormatException.class),
                arguments("contact@test", InvalidEmailFormatException.class),
                arguments("", IllegalArgumentException.class)
        );
    }

    private static Stream<Arguments> invalidEmailsWithNames() {
        return Stream.of(
                arguments(
                        named("no @", "test.email.com"),
                        named("InvalidEmailFormat", InvalidEmailFormatException.class)
                ),
                arguments(
                        named("no @ and no extension", "hello"),
                        named("InvalidEmailFormat", InvalidEmailFormatException.class)
                ),
                arguments(
                        named("no extension", "contact@test"),
                        named("InvalidEmailFormat", InvalidEmailFormatException.class)
                ),
                arguments(
                        named("no characters", ""),
                        named("IllegalArgument", IllegalArgumentException.class)
                )
        );
    }

}