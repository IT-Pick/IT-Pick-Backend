package store.itpick.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import store.itpick.backend.dto.user.user.PostUserRequest;
import store.itpick.backend.service.UserService;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public void getSignUp() {

    }

    @PostMapping("/signup")
    public void postSignUp(PostUserRequest postUserRequest, Model model){
        try{
            System.out.println("UserService" + postUserRequest.getNickname()+ postUserRequest.getId());
            userService.createUser(postUserRequest);
        }catch(Exception e){
            System.out.println(e);
        }

    }
    // 중복 체크 로직
}
