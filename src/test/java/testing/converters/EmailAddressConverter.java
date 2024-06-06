package testing.converters;

import domain.model.EmailAddress;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

public class EmailAddressConverter extends TypedArgumentConverter<String, EmailAddress> {

    protected EmailAddressConverter() {
        super(String.class, EmailAddress.class);
    }

    @Override
    protected EmailAddress convert(String s) throws ArgumentConversionException {
        return new EmailAddress(s);
    }

}
