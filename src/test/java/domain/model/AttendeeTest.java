package domain.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.TypedArgumentConverter;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import testing.arguments_sources.BlankArgumentsSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class AttendeeTest {

    @ParameterizedTest
    @CsvSource({
            "Amy, amy@email.com",
            "Rory, rory@email.com",
    })
    void should_create_attendee(String name, String emailAsString) {
        EmailAddress email = new EmailAddress(emailAsString);
        Attendee attendee = new Attendee(name, email);

        assertThat(attendee.name()).isEqualTo(name);
        assertThat(attendee.email()).isEqualTo(email);
    }

    @ParameterizedTest
    @NullSource
    @ArgumentsSource(BlankArgumentsSource.class)
    void invalid_name(String name) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Attendee.withPersonalInformation(name, "test@email.com"))
                .withMessage("Attendee name cannot be empty");
    }


    static class EmailAddressConverter extends TypedArgumentConverter<String, EmailAddress> {

        protected EmailAddressConverter() {
            super(String.class, EmailAddress.class);
        }

        @Override
        protected EmailAddress convert(String s) throws ArgumentConversionException {
            return new EmailAddress(s);
        }

    }

}