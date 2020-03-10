package ipc.pop3.server.persistence.service;

import ipc.pop3.server.persistence.dao.ConfigurationRepository;
import ipc.pop3.server.persistence.dao.UserRepository;
import ipc.pop3.server.persistence.model.Configuration;
import ipc.pop3.server.persistence.model.User;
import ipc.pop3.server.security.utils.PasswordUtils;
import ipc.pop3.server.utils.constants.ApplicationConstants;
import ipc.pop3.server.utils.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;


    synchronized public Configuration getConfiguration(String configuration) throws InterruptedOperationException, InvalidConfigurationNameException {
        Configuration config;
        try {
            config = configurationRepository.findByConfiguration(configuration); }
        catch (Exception e) {
            throw new InterruptedOperationException("Error while trying to find configuration", e); }
        if (config == null) {
            throw new InvalidConfigurationNameException("No such configuration"); }
        return config; }

    synchronized public Configuration setConfiguration(String configuration, String value) throws InvalidConfigurationNameException {
        if(configuration == null || configuration.isBlank() || configuration.contains(" ")) {
            throw new InvalidConfigurationNameException("Configuration name is not valid"); }
        Configuration newConfig = new Configuration(configuration, value);
        return configurationRepository.save(newConfig);
    }
}
