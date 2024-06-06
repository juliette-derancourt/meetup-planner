package domain.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import testing.converters.EmailAddressConverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class AttendeeTest {

    @ParameterizedTest
    @CsvSource({
            "Amy, amy@email.com",
            "Rory, rory@email.com",
    })
    void should_create_attendee(String name, @ConvertWith(EmailAddressConverter.class) EmailAddress email) {
        Attendee attendee = new Attendee(name, email);
        assertThat(attendee.name()).isEqualTo(name);
        assertThat(attendee.email()).isEqualTo(email);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void invalid_name(String name) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Attendee.withPersonalInformation(name, "test@email.com"))
                .withMessage("Attendee name cannot be empty");
    }

}