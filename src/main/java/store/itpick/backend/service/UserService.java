package store.itpick.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.itpick.backend.common.exception.UserException;
import store.itpick.backend.common.exception.jwt.unauthorized.JwtExpiredTokenException;
import store.itpick.backend.common.exception.jwt.unauthorized.JwtInvalidTokenException;
import store.itpick.backend.common.response.status.BaseExceptionResponseStatus;
import store.itpick.backend.dto.auth.JwtDTO;
import store.itpick.backend.dto.auth.LoginRequest;
import store.itpick.backend.dto.auth.LoginResponse;
import store.itpick.backend.dto.auth.RefreshResponse;
import store.itpick.backend.dto.user.user.PostUserRequest;
import store.itpick.backend.dto.user.user.PostUserResponse;
import store.itpick.backend.jwt.JwtProvider;
import store.itpick.backend.model.User;
import store.itpick.backend.repository.UserRepository;


import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static store.itpick.backend.common.response.status.BaseExceptionResponseStatus.*;
import static store.itpick.backend.common.response.status.BaseExceptionResponseStatus.INVALID_TOKEN;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;


    @Value("${secret.jwt-refresh-expired-in}")
    private long JWT_REFRESH_EXPIRED_IN;






    public LoginResponse login(LoginRequest authRequest) {

        String email = authRequest.getEmail();

        // TODO: 1. 이메일 유효성 확인
        User user;
        try {
            user = userRepository.getUserByEmail(email).get();
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new UserException(EMAIL_NOT_FOUND);
        }
        long userId = user.getUserId();

        // TODO: 2. 비밀번호 일치 확인
        validatePassword(authRequest.getPassword(), userId);

        // TODO: 3. JWT 갱신
        String updatedAccessToken = jwtProvider.createToken(email, userId);
        String updatedRefreshToken = jwtProvider.createRefreshToken(email, userId);
        user.setRefreshToken(updatedRefreshToken);
        userRepository.save(user);
        JwtDTO jwtDTO = new JwtDTO(updatedAccessToken, updatedRefreshToken);

        return new LoginResponse(userId, jwtDTO);
    }

    private void validatePassword(String password, long userId) {
        String encodedPassword = userRepository.getUserByUserId(userId).get().getPassword();
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new UserException(PASSWORD_NO_MATCH);
        }
    }


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
        User user = User.builder().email(postUserRequest.getEmail()).password(encodedPassword).nickname(postUserRequest.getNickname()).birthDate(postUserRequest.getBirth_date()).status("active").build();

        user = userRepository.save(user);

        // 회원가입에서 jwt발급 x -> 회원가입 후 로그인 로직으로 가는게 좋을듯 (일단 토의하기)
        //  이런식이면 return null;

        // 회원가입에서 jwt 생성 시 여기에 로직 포함시키기
        String jwt = "";

        return new PostUserResponse(user.getUserId(), jwt);
    }

    public RefreshResponse refresh(String refreshToken){
        // 만료 & 유효성 확인, 로그아웃 확인
        if(jwtProvider.isExpiredToken(refreshToken) || refreshToken == null || refreshToken.isEmpty()){
            throw new JwtExpiredTokenException(EXPIRED_REFRESH_TOKEN);
        }
        String email = jwtProvider.getPrincipal(refreshToken);
        if (email == null) {
            throw new JwtInvalidTokenException(INVALID_TOKEN);
        }

        // 이메일 유효성 확인
        User user;
        try {
            user = userRepository.getUserByEmail(email).get();
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new UserException(EMAIL_NOT_FOUND);
        }
        long userId = user.getUserId();

        // 엑세스 토큰 재발급
        return new RefreshResponse(jwtProvider.createToken(email, userId));
    }

    // 로그아웃
    public void logout(long userId) {

        User user;
        try {
            user = userRepository.getUserByUserId(userId).get();
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new UserException(USER_NOT_FOUND);
        }
        user.setRefreshToken(null);
        userRepository.save(user);
    }


    public void modifyUserStatus_deleted(long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setStatus("deleted");
            userRepository.save(user);
        } else {
            throw new UserException(BaseExceptionResponseStatus.USER_NOT_FOUND);
        }
    }







    private void validateEmail(String email) {
        if (userRepository.existsByEmailAndStatusIn(email, List.of("active", "dormant"))) {
            throw new UserException(BaseExceptionResponseStatus.DUPLICATE_EMAIL);
        }
    }

    private void validateNickname(String nickname) {
        if (userRepository.existsByNicknameAndStatusIn(nickname, List.of("active", "dormant"))) {
            throw new UserException(BaseExceptionResponseStatus.DUPLICATE_NICKNAME);
        }
    }

    public long getUserIdByEmail(String email) {
        return userRepository.getUserByEmail(email).get().getUserId();
    }

    public void validationUserId(long userId, long header_userId) {
        if (userId == header_userId) {
            throw  new UserException(TOKEN_MISMATCH);
        }
    }


}
