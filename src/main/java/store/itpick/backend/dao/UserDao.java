package store.itpick.backend.dao;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import store.itpick.backend.common.exception.UserException;
import store.itpick.backend.common.response.status.BaseExceptionResponseStatus;
import store.itpick.backend.model.User;
import store.itpick.backend.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDao {
    private final UserRepository userRepository;

    // 주어진 이메일, 상태목록을 기반으로 사용자 존재 유무 확인
    public boolean existsByEmailAndStatusIn(String email, List<String> status) {
        return userRepository.existsByEmailAndStatusIn(email, status);
    }
    // 주어진 닉네임, 상태목록을 기반으로 사용자 존재 확인
    public boolean existsByNicknameAndStatusIn(String nickname, List<String> status) {
        return userRepository.existsByNicknameAndStatusIn(nickname, status);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public int modifyUserStatus_deleted(long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setStatus("deleted");
            userRepository.save(user);
        } else {
            throw new UserException(BaseExceptionResponseStatus.USER_NOT_FOUND);
        }
        return 0;
    }
}