package ipc.pop3.server.controller;

import ipc.pop3.server.persistence.service.UserService;
import ipc.pop3.server.utils.exceptions.InvalidPasswordException;
import ipc.pop3.server.utils.exceptions.InvalidUsernameException;
import ipc.pop3.server.utils.exceptions.UserAlreadyExistingException;
import ipc.pop3.server.utils.model.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/register")
    public Answer registerUser(@RequestParam(value = "username") String username,
                               @RequestParam(value = "userpass") String userpass) {
        Answer ans = new Answer(501, "Not implemented, WIP");
        try {
            userService.registerUser(username, userpass);
        } catch (UserAlreadyExistingException e) {
            return new Answer(400, "Username already exists");
        } catch (InvalidUsernameException e) {
            return new Answer(400, "Username not valid --- " + e.getMessage());
        } catch (InvalidPasswordException e) {
            return new Answer(400, "Userpass not valid --- " + e.getMessage());
        }
        return ans;
    }
}
