package domain.model;

import java.util.regex.Pattern;

public record EmailAddress(String value) {

    private static final Pattern emailPattern = Pattern.compile("^[\\w\\-.]+@([\\w-]+\\.)+[\\w-]{2,}$");

    public EmailAddress {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid email address: " + value);
        }
    }

    public static EmailAddress of(String value) {
        return new EmailAddress(value);
    }

    private static boolean isValid(String emailAddress) {
        if (emailAddress == null) {
            return false;
        }
        return emailPattern.matcher(emailAddress).matches();
    }

    @Override
    public String toString() {
        return value;
    }

}
