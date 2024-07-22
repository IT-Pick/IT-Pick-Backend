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

    public Optional<User> findByEmailAndStatusIn(String email, List<String> status) {
        return userRepository.findByEmailAndStatusIn(email, status);
    }

    public Optional<User> findByNicknameAndStatusIn(String nickname, List<String> status) {
        return userRepository.findByNicknameAndStatusIn(nickname, status);
    }

    public boolean existsByEmailAndStatusIn(String email, List<String> status) {
        return userRepository.existsByEmailAndStatusIn(email, status);
    }

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