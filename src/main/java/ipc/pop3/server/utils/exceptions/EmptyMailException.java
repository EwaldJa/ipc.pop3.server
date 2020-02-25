package ipc.pop3.server.utils.exceptions;

public class EmptyMailException extends Exception {

    public EmptyMailException(String message) {
        super(message);
    }

    public EmptyMailException(String message, Exception cause) {
        super(message, cause);
    }
}
