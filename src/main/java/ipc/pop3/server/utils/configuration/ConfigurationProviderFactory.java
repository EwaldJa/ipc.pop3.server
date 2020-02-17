package ipc.pop3.server.utils.configuration;

import ipc.pop3.server.utils.constants.ApplicationConstants;

public class ConfigurationProviderFactory {

    public static ConfigurationProvider getConfigurationProvider(ApplicationConstants.ConfigurationProviderMode mode) {
        switch (mode) {
            case DATABASE:
                return new DatabaseConfigurationProvider();
            default:
                return new ConstantConfigurationProvider();
        }
    }
}
