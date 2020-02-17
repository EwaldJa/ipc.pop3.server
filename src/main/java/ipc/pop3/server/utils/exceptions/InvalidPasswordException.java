package ipc.pop3.server.utils.exceptions;

public class InvalidPasswordException extends Exception {

    public InvalidPasswordException(String message) {
        super(message);
    }

    public InvalidPasswordException(String message, Exception cause) {
        super(message, cause);
    }
}
