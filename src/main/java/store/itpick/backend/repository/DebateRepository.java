package store.itpick.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import store.itpick.backend.model.Debate;

@Repository
public interface DebateRepository extends JpaRepository<Debate, Long> {


}
