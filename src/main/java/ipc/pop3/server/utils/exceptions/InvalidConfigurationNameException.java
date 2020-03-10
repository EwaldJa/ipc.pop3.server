package ipc.pop3.server.utils.exceptions;

public class InvalidConfigurationNameException extends Exception {

    public InvalidConfigurationNameException(String message) {
        super(message);
    }

    public InvalidConfigurationNameException(String message, Exception cause) {
        super(message, cause);
    }
}
