package ipc.pop3.server.security.utils;

import ipc.pop3.server.utils.configuration.ConfigurationProvider;
import ipc.pop3.server.utils.configuration.ConfigurationProviderFactory;
import ipc.pop3.server.utils.constants.ApplicationConstants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Random;

public class PasswordUtils {

    private static final int SaltSize = 64;
    private static final Random RANDOM = new SecureRandom();
    private static final ConfigurationProvider configurationProvider = ConfigurationProviderFactory.getConfigurationProvider(ApplicationConstants.ConfigurationProviderMode.CONSTANT);

    public static boolean checkMD5HashedPassword(String usermd5pass, Timestamp timestamp) {
        //TODO : check pass
        return false;
    }

    public static String generateUserSalt() {
        byte[] salt = new byte[SaltSize];
        RANDOM.nextBytes(salt);
        return hashBytes(salt);
    }

    public static String getHashedPassString(String userpass, String usersalt) {
        return hashString(usersalt + userpass);
    }

    private static String hashString(String input) {
        return hashBytes(input.getBytes());
    }

    private static String hashBytes(byte[] input) {

        String hashedString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(configurationProvider.getAppSalt().getBytes());
            byte[] bytes = md.digest(input);
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedString = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return null;
        }
        return hashedString;
    }
}
