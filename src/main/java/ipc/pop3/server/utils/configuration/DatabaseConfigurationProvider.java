package ipc.pop3.server.utils.configuration;

import ipc.pop3.server.persistence.model.User;
import ipc.pop3.server.persistence.service.ConfigurationService;
import ipc.pop3.server.security.utils.PasswordUtils;
import ipc.pop3.server.utils.bean.SpringUtils;
import ipc.pop3.server.utils.constants.ApplicationConstants;
import ipc.pop3.server.utils.exceptions.InterruptedOperationException;
import ipc.pop3.server.utils.exceptions.InvalidConfigurationNameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class DatabaseConfigurationProvider implements ConfigurationProvider {

    public String getAppSalt() {return null;}

    private ConfigurationService configurationService = SpringUtils.getBean(ConfigurationService.class);;

    public boolean checkUserPassword(String userpass, User user, Timestamp timestamp) {
        ApplicationConstants.PasswordCheckMode passwordCheckMode = null;
        try {
            passwordCheckMode = ApplicationConstants.PasswordCheckMode.valueOf(configurationService.getConfiguration("passwordCheckMode").getValue());
        } catch (InterruptedOperationException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationNameException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        switch (passwordCheckMode) {
            case CLEAR:
                return (userpass.equals(user.getPassword()));
            default:
            case MD5:
                return PasswordUtils.checkMD5HashedPassword(userpass, user.getPassword(), timestamp);
        }
    }
}
