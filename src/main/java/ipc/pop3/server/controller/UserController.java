package ipc.pop3.server.controller;

import ipc.pop3.server.utils.model.Answer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {


    @PostMapping("/user/register")
    public Answer registerUser(@RequestParam(value = "username") String username,
                               @RequestParam(value = "userpass") String userpass) {
        Answer ans = new Answer(501, "Not implemented, WIP");
        return ans;
    }
}
