package store.itpick.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
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
import store.itpick.backend.dto.auth.PostUserRequest;
import store.itpick.backend.dto.auth.PostUserResponse;
import store.itpick.backend.jwt.JwtProvider;
import store.itpick.backend.model.LikedTopic;
import store.itpick.backend.model.User;
import store.itpick.backend.repository.LikedTopicRepository;
import store.itpick.backend.repository.UserRepository;


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

import static store.itpick.backend.common.response.status.BaseExceptionResponseStatus.*;
import static store.itpick.backend.common.response.status.BaseExceptionResponseStatus.INVALID_TOKEN;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtTokenProvider;
    private final MailService mailService;
    private final RedisService redisService;
    private final LikedTopicRepository likedTopicRepository;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;
    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    private final JwtProvider jwtProvider;




    public LoginResponse login(LoginRequest authRequest) {

        String email = authRequest.getEmail();

        // TODO: 1. 이메일 유효성 확인
        User user;
        try {
            user = userRepository.getUserByEmail(email).get();
        } catch (NoSuchElementException e) {
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
        User user = User.builder().email(postUserRequest.getEmail()).password(encodedPassword).nickname(postUserRequest.getNickname()).birthDate(postUserRequest.getBirth_date()).status("active").alertSetting(true).createAt(Timestamp.valueOf(LocalDateTime.now())).build();

        user = userRepository.save(user);

        if (postUserRequest.getLikedTopics() != null) {
            for (String likedTopic : postUserRequest.getLikedTopics()) {
                LikedTopic newLikedTopic = LikedTopic.builder()
                        .user(user)
                        .liked_topic(likedTopic)
                        .status("active")
                        .createAt(Timestamp.valueOf(LocalDateTime.now()))
                        .build();
                likedTopicRepository.save(newLikedTopic);
            }
        }

        return new PostUserResponse(user.getUserId());
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
        } catch (NoSuchElementException e) {
            throw new UserException(USER_NOT_FOUND);
        }
        user.setRefreshToken(null);
        userRepository.save(user);
    }

    public void modifyUserStatus_deleted(String token) {
        // JWT 토큰에서 사용자 ID 추출
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        // 사용자 상태를 "deleted"로 변경
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setStatus("deleted");
            userRepository.save(user);
        } else {
            throw new UserException(USER_NOT_FOUND);
        }
    }

    public void sendCodeToEmail(String toEmail) {
        this.validateEmail(toEmail);
        String title = "ITPICK 회원가입을 위한 이메일 인증 번호입니다.";
        String authCode = this.createCode();
        mailService.sendEmail(toEmail, title, authCode);
        // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
        redisService.setValues(AUTH_CODE_PREFIX + toEmail,
                authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }

    private String createCode() {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new UserException(BaseExceptionResponseStatus.NO_SUCH_ALGORITHM);
        }
    }

    public void verifiedCode(String email, String authCode) {
        this.validateEmail(email);
        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
        boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);

        if(!authResult){
            throw new UserException(BaseExceptionResponseStatus.AUTH_CODE_IS_NOT_SAME);
        }
    }







    public void validateEmail(String email) {
        if (userRepository.existsByEmailAndStatusIn(email, List.of("active", "dormant"))) {
            throw new UserException(BaseExceptionResponseStatus.DUPLICATE_EMAIL);
        }
    }

    public void validateNickname(String nickname) {
        if (userRepository.existsByNicknameAndStatusIn(nickname, List.of("active", "dormant"))) {
            throw new UserException(BaseExceptionResponseStatus.DUPLICATE_NICKNAME);
        }
    }

    public long getUserIdByEmail(String email) {
        return userRepository.getUserByEmail(email).get().getUserId();
    }


}
