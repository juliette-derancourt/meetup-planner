package domain.model.exceptions;

public class InvalidEmailFormatException extends RuntimeException {

    public InvalidEmailFormatException(String email) {
        super("Invalid email address: " + email);
    }

}
