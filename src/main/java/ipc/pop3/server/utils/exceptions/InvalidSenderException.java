package ipc.pop3.server.utils.exceptions;

public class InvalidSenderException extends Exception {

    public InvalidSenderException(String message) {
        super(message);
    }

    public InvalidSenderException(String message, Exception cause) {
        super(message, cause);
    }
}
