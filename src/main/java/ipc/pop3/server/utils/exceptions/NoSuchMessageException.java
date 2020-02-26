package ipc.pop3.server.utils.exceptions;

public class NoSuchMessageException extends Exception {

    public NoSuchMessageException(String message) {
        super(message);
    }

    public NoSuchMessageException(String message, Exception cause) {
        super(message, cause);
    }
}
