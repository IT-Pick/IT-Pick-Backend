package store.itpick.backend.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import store.itpick.backend.common.argument_resolver.PreAuthorize;
import store.itpick.backend.common.exception.AuthException;
import store.itpick.backend.common.response.BaseResponse;
import store.itpick.backend.dto.user.*;
import store.itpick.backend.service.S3ImageBucketService;
import store.itpick.backend.service.UserService;

import static store.itpick.backend.common.response.status.BaseExceptionResponseStatus.INVALID_USER_VALUE;
import static store.itpick.backend.util.BindingResultUtils.getErrorMessages;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final S3ImageBucketService s3ImageBucketService;

    @PatchMapping("/nickname")
    public BaseResponse<?> changeNickname(@PreAuthorize long userId, @Validated @RequestBody NicknameRequest nicknameRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new AuthException(INVALID_USER_VALUE, getErrorMessages(bindingResult));
        }
        userService.changeNickname(userId, nicknameRequest.getNickname());
        return new BaseResponse<>(null);
    }

    /*
    @PatchMapping("/birth-date")
    public BaseResponse<?> changeBrithDate(@PreAuthorize long userId, @Validated @RequestBody BirthDateRequest birthDateRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new UserException(INVALID_USER_VALUE, getErrorMessages(bindingResult));
        }
        userService.changeBirthDate(userId, birthDateRequest.getBirth_date());
        return new BaseResponse<>(null);
    }

     */

    @PatchMapping("/liked-topics")
    public BaseResponse<?> changeLikedTopics(@PreAuthorize long userId, @Validated @RequestBody LikedTopicsRequest likedTopicsRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new AuthException(INVALID_USER_VALUE, getErrorMessages(bindingResult));
        }
        log.info(String.valueOf(userId));
        userService.changeLikedTopics(userId, likedTopicsRequest.getLikedTopicList());
        return new BaseResponse<>(null);
    }

    /*
    @PatchMapping("/email")
    public BaseResponse<?> changeEmail(@PreAuthorize long userId, @Validated @RequestBody EmailRequest emailRequest, BindingResult bindingResult, @PreAccessToken String AccessToken){
        if (bindingResult.hasErrors()) {
            throw new UserException(INVALID_USER_VALUE, getErrorMessages(bindingResult));
        }
        return new BaseResponse<>(userService.changeEmail(userId, emailRequest.getEmail(), AccessToken, emailRequest.getRefreshToken()));
    }
     */

    @PatchMapping("/password")
    public BaseResponse<?> changePassword(@PreAuthorize long userId, @Validated @RequestBody PasswordRequest passwordRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new AuthException(INVALID_USER_VALUE, getErrorMessages(bindingResult));
        }
        userService.changePassword(userId, passwordRequest.getPassword());
        return new BaseResponse<>(null);
    }

    @PostMapping("/profile-img")
    public BaseResponse<ProfileImgResponse> changeProfileImg(@PreAuthorize long userId,@RequestParam("file") MultipartFile file){
        String previousImgUrl = userService.getProfileImgUrl(userId);
        if (previousImgUrl != null)  {
            s3ImageBucketService.deleteImage(previousImgUrl);
        }
        String imgUrl = s3ImageBucketService.saveProfileImg(file);
        userService.changeProfileImg(userId, imgUrl);
        return new BaseResponse<>(new ProfileImgResponse(imgUrl));
    }

}
