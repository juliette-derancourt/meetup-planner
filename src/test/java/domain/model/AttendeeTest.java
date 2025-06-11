package domain.model;

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
import static org.junit.jupiter.params.provider.Arguments.arguments;

class AttendeeTest {

    @ParameterizedTest
    @CsvSource({
            "Amy, amy@email.com",
            "Rory, rory@email.com",
    })
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
                .isThrownBy(() -> Attendee.withPersonalInformation(name, "test@email.com"))
                .withMessage("Attendee name cannot be empty");
    }

}