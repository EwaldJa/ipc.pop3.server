package ipc.pop3.server.persistence.service;

import ipc.pop3.server.persistence.dao.UserRepository;
import ipc.pop3.server.persistence.model.User;
import ipc.pop3.server.security.utils.PasswordUtils;
import ipc.pop3.server.utils.constants.ApplicationConstants;
import ipc.pop3.server.utils.exceptions.InvalidPasswordException;
import ipc.pop3.server.utils.exceptions.InvalidUsernameException;
import ipc.pop3.server.utils.exceptions.UserAlreadyExistingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User logUser(String username, String userpass) throws InvalidUsernameException, InvalidPasswordException {
        User currUser;
        try {
            currUser = userRepository.findByUsername(username); }
        catch (Exception e) {
            throw new InvalidUsernameException("No such user", e); }

        if (currUser.getPassword().equals(PasswordUtils.getHashedPassString(userpass, currUser.getUsersalt()))) {
            return currUser; }
        else throw new InvalidPasswordException("Password does not match");
    }

    public User registerUser(String username, String userpass) throws UserAlreadyExistingException, InvalidUsernameException, InvalidPasswordException {
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

        User existingUser = null;
        try {
            existingUser = userRepository.findByUsername(username); }
        catch (Exception ignored) {}
        if (existingUser != null) {
            throw new UserAlreadyExistingException("This username is already taken"); }

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


        String usersalt = PasswordUtils.generateUserSalt();
        User newUser = new User(username, PasswordUtils.getHashedPassString(userpass, usersalt), usersalt);
        return userRepository.save(newUser);
    }
}
