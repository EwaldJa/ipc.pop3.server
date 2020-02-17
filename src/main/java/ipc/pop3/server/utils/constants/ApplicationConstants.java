package ipc.pop3.server.utils.constants;

public class ApplicationConstants {

    public enum ConfigurationProviderMode {
        CONSTANT,
        DATABASE
    }

    public static final int MIN_USERNAME_LENGTH = 6;
    public static final int MAX_USERNAME_LENGTH = 100;

    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 150;

}
