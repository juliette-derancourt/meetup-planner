package domain.model;

import domain.model.exceptions.InvalidEmailFormatException;

import java.util.regex.Pattern;

public record EmailAddress(String value) {

    private static final Pattern emailPattern = Pattern.compile("^[\\w\\-.]+@([\\w-]+\\.)+[\\w-]{2,}$");

    public EmailAddress {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is null or empty");
        }
        if (!isValid(value)) {
            throw new InvalidEmailFormatException(value);
        }
    }

    public static EmailAddress of(String value) {
        return new EmailAddress(value);
    }

    private static boolean isValid(String emailAddress) {
        return emailPattern.matcher(emailAddress).matches();
    }

    @Override
    public String toString() {
        return value;
    }

}
