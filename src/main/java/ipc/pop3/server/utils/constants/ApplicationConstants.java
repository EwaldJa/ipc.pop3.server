package ipc.pop3.server.utils.constants;

public class ApplicationConstants {

    public static final String POP3_SERVER_NAME = "ipcmail";

    public enum ConfigurationProviderMode {
        CONSTANT,
        DATABASE
    }

    public static final ConfigurationProviderMode DEFAULT_MODE = ConfigurationProviderMode.DATABASE;

    public static final int MIN_USERNAME_LENGTH = 6;
    public static final int MAX_USERNAME_LENGTH = 100;

    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 150;

    public enum PasswordCheckMode {
        CLEAR,
        MD5
    }

}
