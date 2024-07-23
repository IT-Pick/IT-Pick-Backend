package store.itpick.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import store.itpick.backend.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmailAndStatusIn(String email, List<String> status);

    boolean existsByNicknameAndStatusIn(String nickname, List<String> status);
}
