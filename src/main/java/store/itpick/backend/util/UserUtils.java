package store.itpick.backend.util;

import org.springframework.beans.factory.annotation.Autowired;
import store.itpick.backend.common.exception.UserException;
import store.itpick.backend.model.User;
import store.itpick.backend.repository.UserRepository;

import java.util.NoSuchElementException;

import static store.itpick.backend.common.response.status.BaseExceptionResponseStatus.USER_NOT_FOUND;

public class UserUtils {
    public static User getUser(long userId, UserRepository userRepository){
        try {
            return userRepository.getUserByUserId(userId).get();
        } catch (NoSuchElementException e) {
            throw new UserException(USER_NOT_FOUND);
        }
    }
}
