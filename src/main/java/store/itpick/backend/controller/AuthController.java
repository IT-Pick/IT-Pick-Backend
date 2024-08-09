package store.itpick.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import store.itpick.backend.common.argument_resolver.PreAuthorize;
import store.itpick.backend.common.response.BaseResponse;
import store.itpick.backend.dto.auth.LoginRequest;
import store.itpick.backend.dto.auth.LoginResponse;
import store.itpick.backend.dto.auth.RefreshRequest;
import store.itpick.backend.dto.auth.RefreshResponse;
import store.itpick.backend.dto.auth.PostUserRequest;
import store.itpick.backend.dto.auth.PostUserResponse;
import store.itpick.backend.service.AuthService;
import store.itpick.backend.common.exception.UserException;

import  static store.itpick.backend.common.response.status.BaseExceptionResponseStatus.INVALID_USER_VALUE;
import static store.itpick.backend.util.BindingResultUtils.getErrorMessages;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private final AuthService authService;

    @PostMapping("/refresh")
    public BaseResponse<RefreshResponse> refresh(@Validated @RequestBody RefreshRequest refreshRequest) {
        return new BaseResponse<>(authService.refresh(refreshRequest.getRefreshToken()));
    }


    /**
     * 로그인
     */
    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@Validated @RequestBody LoginRequest authRequest, BindingResult bindingResult) {
        log.info("[AuthController.login]");
        if (bindingResult.hasErrors()) {
            throw new UserException(INVALID_USER_VALUE, getErrorMessages(bindingResult));
        }
        return new BaseResponse<>(authService.login(authRequest));
    }

    /**
     * 로그아웃 : db의 refresh 토큰을 null로 설정
     */
    @PatchMapping("/logout")
    public BaseResponse<Object> logout(@PreAuthorize long userId) {
        authService.logout(userId);
        return new BaseResponse<>(null);
    }


    @PostMapping("/signup")
    public BaseResponse<PostUserResponse> signUp(@Valid @RequestBody PostUserRequest postUserRequest, BindingResult bindingResult) {
      if(bindingResult.hasErrors()){
          throw new UserException(INVALID_USER_VALUE, getErrorMessages(bindingResult));
      }
      return new BaseResponse<>(authService.signUp(postUserRequest));
    }

    @GetMapping("/email/check")
    public BaseResponse<Object> checkEmailDuplicate(@RequestParam("email") @Valid String email) {
        authService.validateEmail(email);
        return new BaseResponse<>(HttpStatus.OK);
    }

    @GetMapping("/nickname/check")
    public BaseResponse<Object> checkNicknameDuplicate(@RequestParam("nickname") @Valid String nickname) {
        authService.validateNickname(nickname);
        return new BaseResponse<>(HttpStatus.OK);
    }

    @DeleteMapping("")
    public BaseResponse<Object> modifyUserStatus_deleted(@RequestHeader("Authorization") String token) {
        // Authorization 헤더에서 "Bearer " 부분을 제거하고 토큰만 추출
        String accessToken = token.replace("Bearer ", "");
        authService.modifyUserStatus_deleted(accessToken);
        return new BaseResponse<>(HttpStatus.OK);
    }

    @PostMapping("/emails/verification-requests")
    public BaseResponse<Object> sendMessage(@RequestParam("email") @Valid String email){
        authService.sendCodeToEmail(email);

        return new BaseResponse<>(HttpStatus.OK);
    }

    @GetMapping("/emails/verifications")
    public BaseResponse<Object> verificationEmail(@RequestParam("email") @Valid String email,
                                            @RequestParam("code") String authCode){
        authService.verifiedCode(email,authCode);

        return new BaseResponse<>(HttpStatus.OK);
    }
}
