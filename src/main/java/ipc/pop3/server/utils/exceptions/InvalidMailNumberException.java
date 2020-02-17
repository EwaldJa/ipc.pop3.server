package ipc.pop3.server.utils.exceptions;

public class InvalidMailNumberException extends Exception {

    public InvalidMailNumberException(String message) {
        super(message);
    }

    public InvalidMailNumberException(String message, Exception cause) {
        super(message, cause);
    }
}
