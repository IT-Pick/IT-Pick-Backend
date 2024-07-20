package store.itpick.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import store.itpick.backend.dto.user.user.PostUserRequest;
import store.itpick.backend.dto.user.user.PostUserResponse;
import store.itpick.backend.service.UserService;
import store.itpick.backend.common.exception.UserException;
import store.itpick.backend.common.response.status.BaseExceptionResponseStatus;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String getSignUp() {
        return "signup"; // Return the signup view name
    }

    @PostMapping("/signup")
    public String postSignUp(PostUserRequest postUserRequest, Model model) {
        try {
            System.out.println("UserService: " + postUserRequest.getNickname() + " " + postUserRequest.getEmail());
            PostUserResponse response = userService.signUp(postUserRequest);
            model.addAttribute("userId", response.getUserId());
            model.addAttribute("jwt", response.getJwt());
            return "signupSuccess"; // Return the signup success view name
        } catch (UserException e) {
            System.out.println(e);
            model.addAttribute("error", e.getMessage());
            return "signup"; // Return the signup view with error message
        } catch (Exception e) {
            System.out.println(e);
            model.addAttribute("error", "Unexpected error occurred.");
            return "signup"; // Return the signup view with error message
        }
    }
}
