package store.itpick.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import store.itpick.backend.dto.auth.JwtDTO;
import store.itpick.backend.jwt.JwtProvider;
import store.itpick.backend.model.LikedTopic;
import store.itpick.backend.model.User;
import store.itpick.backend.repository.LikedTopicRepository;
import store.itpick.backend.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static store.itpick.backend.util.UserUtils.getUser;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LikedTopicRepository likedTopicRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;


    public void changeNickname(long userId, String nickname) {
        User user = getUser(userId, userRepository);
        user.setNickname(nickname);
        user.setUpdateAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

    }

    public void changeBirthDate(long userId, String birth_date) {
        User user = getUser(userId, userRepository);
        user.setBirthDate(birth_date);
        user.setUpdateAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);
    }

    public void changeLikedTopics(long userId, List<String> likedTopicList) {
        User user = getUser(userId, userRepository);

        // 기존 LikedTopic 목록을 맵으로 변환
        Map<String, LikedTopic> existingLikedTopicsMap = user.getLikedTopics()
                .stream()
                .collect(Collectors.toMap(LikedTopic::getLiked_topic, likedTopic -> likedTopic));

        // 모든 기존 LikedTopic의 상태를 Inactive로 설정
        for (LikedTopic likedTopic : existingLikedTopicsMap.values()) {
            likedTopic.setStatus("Inactive");
            likedTopicRepository.save(likedTopic);
        }

        // 새로운 likedTopicIdList를 기준으로 LikedTopic을 업데이트
        for (String sendlikedTopic : likedTopicList) {
            LikedTopic likedTopic = existingLikedTopicsMap.get(sendlikedTopic);
            if (likedTopic != null) {
                // 기존에 있는 likedTopic의 상태를 Active로 변경
                likedTopic.setStatus("Active");
                likedTopic.setUpdateAt(Timestamp.valueOf(LocalDateTime.now()));
                likedTopicRepository.save(likedTopic);
            } else {
                // 새로운 likedTopic을 생성하여 추가
                LikedTopic newLikedTopic = LikedTopic.builder()
                        .status("Active")
                        .createAt(Timestamp.valueOf(LocalDateTime.now()))
                        .user(user)
                        .liked_topic(sendlikedTopic)
                        .build();
                user.getLikedTopics().add(newLikedTopic);
                likedTopicRepository.save(newLikedTopic);
            }
        }
        user.setUpdateAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);
    }

    public JwtDTO changeEmail(long userId, String email, String accessToken, String refreshToken) {
        User user = getUser(userId, userRepository);
        user.setEmail(email);
        user.setUpdateAt(new Timestamp(System.currentTimeMillis()));

        String new_accessToken = jwtProvider.createToken_changeEmail(email, userId, accessToken);
        String new_refreshToken = jwtProvider.createToken_changeEmail(email, userId, refreshToken);

        user.setRefreshToken(new_refreshToken);
        userRepository.save(user);


        return new JwtDTO(new_accessToken, new_refreshToken);


    }

    public void changePassword(long userId, String password) {
        User user = getUser(userId, userRepository);
        // Encrypt password
        user.setPassword(passwordEncoder.encode(password));
        user.setUpdateAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(user);
    }

    public void changeProfileImg(long userId, String imgUrl) {
        User user = getUser(userId, userRepository);
        // Encrypt password
        user.setImageUrl(imgUrl);
        user.setUpdateAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(user);
    }


    public String getProfileImgUrl(long userId) {
        User user = getUser(userId, userRepository);
        return user.getImageUrl();
    }
}
