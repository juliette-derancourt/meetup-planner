package domain.model;

import domain.model.exceptions.InvalidEmailFormatException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import testing.arguments_sources.BlankArgumentsProvider;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class AttendeeTest {

    @ParameterizedTest
    @CsvSource({
            "Amy, amy@email.com",
            "Rory, rory@email.com",
    })
    void should_create_attendee(String name, EmailAddress email) {
        Attendee attendee = new Attendee(name, email);

        assertThat(attendee.name()).isEqualTo(name);
        assertThat(attendee.email()).isEqualTo(email);
    }


    //


    @ParameterizedTest
    @NullSource
    @ArgumentsSource(BlankArgumentsProvider.class)
    void invalid_name(String name) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Attendee.withPersonalInformation(name, "amy@email.com"))
                .withMessage("Attendee name cannot be empty");
    }


    //


    @ParameterizedTest
    @MethodSource
    void invalid_email(String email, Class<?> expectedException) {
        assertThatThrownBy(() -> Attendee.withPersonalInformation("Amy", email))
                .isInstanceOf(expectedException);
    }

    private static Stream<Arguments> invalid_email() {
        return Stream.of(
                arguments("test.email.com", InvalidEmailFormatException.class),
                arguments("hello", InvalidEmailFormatException.class),
                arguments("contact@test", InvalidEmailFormatException.class),
                arguments("", IllegalArgumentException.class)
        );
    }

}