package store.itpick.backend.service;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Service;
import store.itpick.backend.common.exception.UserException;
import store.itpick.backend.model.LikedTopic;
import store.itpick.backend.model.User;
import store.itpick.backend.repository.LikedTopicRepository;
import store.itpick.backend.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static store.itpick.backend.common.response.status.BaseExceptionResponseStatus.USER_NOT_FOUND;
import static store.itpick.backend.util.UserUtils.getUser;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LikedTopicRepository likedTopicRepository;


    public void changeNickname(long userId, String nickname) {
        User user = getUser(userId, userRepository);
        user.setNickname(nickname);
        userRepository.save(user);

    }

    public void changeBirthDate(long userId, String birth_date) {
        User user = getUser(userId, userRepository);
        user.setBirthDate(birth_date);
        userRepository.save(user);
    }

    public void changeLikedTopics(long userId, List<Long> likedTopicIdList) {
        User user = getUser(userId, userRepository);

        // 기존 LikedTopic 목록을 맵으로 변환
        Map<Long, LikedTopic> existingLikedTopicsMap = user.getLikedTopics()
                .stream()
                .collect(Collectors.toMap(LikedTopic::getLikedTopicId, likedTopic -> likedTopic));

        // 모든 기존 LikedTopic의 상태를 Inactive로 설정
        for (LikedTopic likedTopic : existingLikedTopicsMap.values()) {
            likedTopic.setStatus("Inactive");
            likedTopicRepository.save(likedTopic);
        }

        // 새로운 likedTopicIdList를 기준으로 LikedTopic을 업데이트
        for (Long likedTopicId : likedTopicIdList) {
            LikedTopic likedTopic = existingLikedTopicsMap.get(likedTopicId);
            if (likedTopic != null) {
                // 기존에 있는 likedTopic의 상태를 Active로 변경
                likedTopic.setStatus("Active");
                likedTopic.setUpdateAt(Timestamp.valueOf(LocalDateTime.now()));
                likedTopicRepository.save(likedTopic);
            } else {
                // 새로운 likedTopic을 생성하여 추가
                LikedTopic newLikedTopic = LikedTopic.builder()
                        .likedTopicId(likedTopicId)
                        .status("Active")
                        .createAt(Timestamp.valueOf(LocalDateTime.now()))
                        .user(user)
                        .build();
                user.getLikedTopics().add(newLikedTopic);
                likedTopicRepository.save(newLikedTopic);
            }
        }
        userRepository.save(user);
    }

    public void changeEmail(long userId, String email) {
        User user = getUser(userId, userRepository);
        user.setEmail(email);
        userRepository.save(user);
    }

    public void changePassword(long userId, String password) {
        User user = getUser(userId, userRepository);
        user.setPassword(password);
        userRepository.save(user);
    }
}
