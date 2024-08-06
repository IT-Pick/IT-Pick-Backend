package store.itpick.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.itpick.backend.model.Vote;

public interface VoteRepository extends JpaRepository<Vote,Long> {
}
