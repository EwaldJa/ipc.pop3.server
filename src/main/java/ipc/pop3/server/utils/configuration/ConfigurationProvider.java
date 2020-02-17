package ipc.pop3.server.utils.configuration;

import ipc.pop3.server.utils.constants.ApplicationConstants;

public interface ConfigurationProvider {

    String getAppSalt();

    default int getMinUsernameLength() { return ApplicationConstants.MIN_USERNAME_LENGTH; }
    default int getMaxUsernameLength() { return ApplicationConstants.MAX_USERNAME_LENGTH; }
    default int getMinPasswordLength() { return ApplicationConstants.MIN_PASSWORD_LENGTH; }
    default int getMaxPasswordLength() { return ApplicationConstants.MAX_PASSWORD_LENGTH; }
}
