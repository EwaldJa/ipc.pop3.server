package ipc.pop3.server.utils.configuration;

import ipc.pop3.server.persistence.model.User;
import ipc.pop3.server.security.utils.PasswordUtils;
import ipc.pop3.server.utils.constants.ApplicationConstants;

import java.sql.Timestamp;

import static ipc.pop3.server.utils.constants.ApplicationConstants.PasswordCheckMode.*;

public class ConstantConfigurationProvider implements ConfigurationProvider {

    private static final String APPLICATION_SALT = "";
    private static final ApplicationConstants.PasswordCheckMode passwordCheckMode = CLEAR;

    public String getAppSalt() {return APPLICATION_SALT;}

    public boolean checkUserPassword(String userpass, User user, Timestamp timestamp) {
        switch (passwordCheckMode) {
            case CLEAR:
                return (userpass.equals(user.getPassword()));
            default:
            case MD5:
                return PasswordUtils.checkMD5HashedPassword(userpass, user.getPassword(), timestamp);
        }
    }
}
