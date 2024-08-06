package store.itpick.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.itpick.backend.model.VoteOption;

public interface VoteOptionRepository extends JpaRepository<VoteOption,Long> {
}
