package store.itpick.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.itpick.backend.common.exception.UserException;
import store.itpick.backend.common.response.status.BaseExceptionResponseStatus;
import store.itpick.backend.dao.UserDao;
import store.itpick.backend.dto.user.user.PostUserRequest;
import store.itpick.backend.dto.user.user.PostUserResponse;
import store.itpick.backend.model.User;


import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public PostUserResponse signUp(PostUserRequest postUserRequest) {
        // email, name 유효성 검사
        validateEmail(postUserRequest.getEmail());
        if (postUserRequest.getNickname() != null) {
            validateNickname(postUserRequest.getNickname());
        }

        // Encrypt password
        String encodedPassword = passwordEncoder.encode(postUserRequest.getPassword());
        postUserRequest.setPassword(encodedPassword);

        // Create user
        User user = User.builder().email(postUserRequest.getEmail()).password(encodedPassword).nickname(postUserRequest.getNickname()).birth_date(postUserRequest.getBirth_date()).status("active").build();

        user = userDao.save(user);

        // 회원가입에서 jwt 생성 시 여기에 로직 포함시키기
        String jwt = "";

        return new PostUserResponse(user.getId(), jwt);
    }

    private void validateEmail(String email) {
        if (userDao.existsByEmailAndStatusIn(email, List.of("active", "dormant"))) {
            throw new UserException(BaseExceptionResponseStatus.DUPLICATE_EMAIL);
        }
    }

    private void validateNickname(String nickname) {
        if (userDao.existsByNicknameAndStatusIn(nickname, List.of("active", "dormant"))) {
            throw new UserException(BaseExceptionResponseStatus.DUPLICATE_NICKNAME);
        }
    }

    public void modifyUserStatus_deleted(long userId) {
        userDao.modifyUserStatus_deleted(userId);
    }
}
