package domain.model;

import domain.model.exceptions.InvalidEmailFormatException;
import net.datafaker.Faker;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import testing.arguments_sources.BlankSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class AttendeeTest {

    @ParameterizedTest
    @MethodSource("attendees")
    void should_create_attendee(String name, EmailAddress email) {
        Attendee attendee = new Attendee(name, email);

        assertThat(attendee.name()).isEqualTo(name);
        assertThat(attendee.email()).isEqualTo(email);
    }

    private static Stream<Arguments> attendees() {
        Faker faker = new Faker();
        return faker.stream(() -> arguments(
                        faker.name().firstName(),
                        faker.internet().emailAddress()
                ))
                .len(5)
                .generate();
    }

    @ParameterizedTest
    @NullSource
    @BlankSource
    void invalid_name(String name) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Attendee.withPersonalInformation(name, "amy@email.com"))
                .withMessage("Attendee name cannot be empty");
    }

    @ParameterizedTest(name = "throws {1} when it contains {0}")
    @MethodSource
    void invalid_email(String email, Class<?> expectedException, String message) {
        assertThatThrownBy(() -> Attendee.withPersonalInformation("Amy", email))
                .isInstanceOf(expectedException)
                .hasMessage(message);
    }

    private static Stream<Arguments> invalid_email() {
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