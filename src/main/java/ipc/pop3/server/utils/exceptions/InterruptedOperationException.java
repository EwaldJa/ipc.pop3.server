package ipc.pop3.server.utils.exceptions;

public class InterruptedOperationException extends Exception {

    public InterruptedOperationException(String message) {
        super(message);
    }

    public InterruptedOperationException(String message, Exception cause) {
        super(message, cause);
    }
}
