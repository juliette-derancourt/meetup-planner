package domain.model.exceptions;

public class UnauthorizedActionException extends RuntimeException {

    public UnauthorizedActionException(String actionName) {
        super("User is not authorized to perform action: " + actionName);
    }

}
