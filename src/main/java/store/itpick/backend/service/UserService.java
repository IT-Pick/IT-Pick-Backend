package store.itpick.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.itpick.backend.common.exception.UserException;
import store.itpick.backend.common.response.status.BaseExceptionResponseStatus;
import store.itpick.backend.dto.auth.LoginRequest;
import store.itpick.backend.dto.auth.LoginResponse;
import store.itpick.backend.dto.user.user.PostUserRequest;
import store.itpick.backend.dto.user.user.PostUserResponse;
import store.itpick.backend.jwt.JwtProvider;
import store.itpick.backend.model.User;
import store.itpick.backend.repository.UserRepository;


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static store.itpick.backend.common.response.status.BaseExceptionResponseStatus.EMAIL_NOT_FOUND;
import static store.itpick.backend.common.response.status.BaseExceptionResponseStatus.PASSWORD_NO_MATCH;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtTokenProvider;
    private final MailService mailService;
    private final RedisService redisService;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;
    private static final String AUTH_CODE_PREFIX = "AuthCode ";


    public LoginResponse login(LoginRequest authRequest) {

        String email = authRequest.getEmail();

        // TODO: 1. 이메일 유효성 확인
        long userId;
        try {
            userId = userRepository.getUserByEmail(email).get().getUserId();
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new UserException(EMAIL_NOT_FOUND);
        }

        // TODO: 2. 비밀번호 일치 확인
        validatePassword(authRequest.getPassword(), userId);

        // TODO: 3. JWT 갱신
        String updatedJwt = jwtTokenProvider.createToken(email, userId);

        return new LoginResponse(userId, updatedJwt);
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

        // 회원가입에서 jwt 생성 시 여기에 로직 포함시키기
        String jwt = "";

        return new PostUserResponse(user.getUserId(), jwt);
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

    public void sendCodeToEmail(String toEmail) {
        this.validateEmail(toEmail);
        String title = "ITPICK 이메일 인증 번호";
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

}
