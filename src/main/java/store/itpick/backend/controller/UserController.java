package store.itpick.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import store.itpick.backend.common.response.BaseResponse;
import store.itpick.backend.dto.user.user.PostUserRequest;
import store.itpick.backend.dto.user.user.PostUserResponse;
import store.itpick.backend.service.UserService;
import store.itpick.backend.common.exception.UserException;


import  static store.itpick.backend.common.response.status.BaseExceptionResponseStatus.INVALID_USER_VALUE;
import static store.itpick.backend.util.BindingResultUtils.getErrorMessages;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;


    @PostMapping("/signup")
    public BaseResponse<PostUserResponse> signUp(@RequestBody PostUserRequest postUserRequest, BindingResult bindingResult) {
      if(bindingResult.hasErrors()){
          throw new UserException(INVALID_USER_VALUE, getErrorMessages(bindingResult));
      }
      return new BaseResponse<>(userService.signUp(postUserRequest));
    }

    @PatchMapping("/{userId}/deleted")
    public BaseResponse<Object> modifyUserStatus_deleted(@PathVariable Long userId) {
        userService.modifyUserStatus_deleted(userId);
        return new BaseResponse<>(null);
    }
}
