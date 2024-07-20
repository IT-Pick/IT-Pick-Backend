package store.itpick.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.itpick.backend.model.User;


public interface UserRepository extends JpaRepository<User, Long> {
}
