package ipc.pop3.server.utils.exceptions;

public class UserAlreadyExistingException extends Exception {

    public UserAlreadyExistingException(String message) {
        super(message);
    }

    public UserAlreadyExistingException(String message, Exception cause) {
        super(message, cause);
    }
}
