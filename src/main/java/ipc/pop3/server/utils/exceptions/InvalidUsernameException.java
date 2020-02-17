package ipc.pop3.server.utils.exceptions;

public class InvalidUsernameException extends Exception {

    public InvalidUsernameException(String message) {
        super(message);
    }

    public InvalidUsernameException(String message, Exception cause) {
        super(message, cause);
    }
}
