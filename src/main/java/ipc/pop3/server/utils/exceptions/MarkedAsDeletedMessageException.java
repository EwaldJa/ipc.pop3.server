package ipc.pop3.server.utils.exceptions;

public class MarkedAsDeletedMessageException extends Exception {

    public MarkedAsDeletedMessageException(String message) {
        super(message);
    }

    public MarkedAsDeletedMessageException(String message, Exception cause) {
        super(message, cause);
    }
}
