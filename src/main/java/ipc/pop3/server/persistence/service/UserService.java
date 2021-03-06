package ipc.pop3.server.persistence.service;

import ipc.pop3.server.persistence.dao.UserRepository;
import ipc.pop3.server.persistence.model.User;
import ipc.pop3.server.utils.configuration.ConfigurationProviderFactory;
import ipc.pop3.server.utils.constants.ApplicationConstants;
import ipc.pop3.server.utils.exceptions.InterruptedOperationException;
import ipc.pop3.server.utils.exceptions.InvalidPasswordException;
import ipc.pop3.server.utils.exceptions.InvalidUsernameException;
import ipc.pop3.server.utils.exceptions.UserAlreadyExistingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    synchronized public User logUser(String username, String usermd5pass, Timestamp timestamp) throws InvalidUsernameException, InvalidPasswordException, InterruptedOperationException {
        User currUser;
        try {
            currUser = userRepository.findByUsername(username); }
        catch (Exception e) {
            throw new InterruptedOperationException("Error while trying to find user", e); }
        if (currUser == null) {
            throw new InvalidUsernameException("No such user"); }
        if (ConfigurationProviderFactory.getConfigurationProvider(ApplicationConstants.DEFAULT_MODE).checkUserPassword(usermd5pass, currUser, timestamp)) {
            return currUser; }
        else throw new InvalidPasswordException("Password does not match");
    }

    synchronized public User registerUser(String username, String userpass) throws UserAlreadyExistingException, InvalidUsernameException, InvalidPasswordException {
        checkUsernameValidity(username);

        User existingUser = null;
        try {
            existingUser = userRepository.findByUsername(username); }
        catch (Exception ignored) {}
        if (existingUser != null) {
            throw new UserAlreadyExistingException("This username is already taken"); }

        checkPasswordValidity(userpass);

        User newUser = new User(username, userpass);
        return userRepository.save(newUser);
    }

    private void checkUsernameValidity(String username) throws InvalidUsernameException {
        if (username == null) {
            throw new InvalidUsernameException("Username is null"); }
        else if (username.isEmpty()) {
            throw new InvalidUsernameException("Username is empty"); }
        else if (username.contains(" ")) {
            throw new InvalidUsernameException("Username contains spaces"); }
        else if (username.length() < ApplicationConstants.MIN_USERNAME_LENGTH) {
            throw new InvalidUsernameException("Username is too short (length must be "+ApplicationConstants.MIN_USERNAME_LENGTH+" at least)"); }
        else if (username.length() > ApplicationConstants.MAX_USERNAME_LENGTH) {
            throw new InvalidUsernameException("Username is too long (length must be "+ApplicationConstants.MAX_USERNAME_LENGTH+" maximum)"); }

    }

    private void checkPasswordValidity(String userpass) throws InvalidPasswordException {
        if (userpass == null) {
            throw new InvalidPasswordException("Password is null"); }
        else if (userpass.isEmpty()) {
            throw new InvalidPasswordException("Password is empty"); }
        else if (userpass.contains(" ")) {
            throw new InvalidPasswordException("Password contains spaces"); }
        else if (userpass.length() < ApplicationConstants.MIN_PASSWORD_LENGTH) {
            throw new InvalidPasswordException("Password is too short (length must be "+ApplicationConstants.MIN_PASSWORD_LENGTH+" at least)"); }
        else if (userpass.length() > ApplicationConstants.MAX_PASSWORD_LENGTH) {
            throw new InvalidPasswordException("Password is too long (length must be "+ApplicationConstants.MAX_PASSWORD_LENGTH+" maximum)"); }

    }
}
