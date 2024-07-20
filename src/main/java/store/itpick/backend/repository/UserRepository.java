package store.itpick.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import store.itpick.backend.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailAndStatusIn(String email, List<String> status);

    Optional<User> findByNicknameAndStatusIn(String nickname,List<String> status);

    boolean existsByEmailAndStatusIn(String email, List<String> status);

    boolean existsByNicknameAndStatusIn(String nickname, List<String> status);
}
