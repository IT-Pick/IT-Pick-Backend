package store.itpick.backend.repository;

import org.springframework.data.repository.CrudRepository;
import store.itpick.backend.dto.redis.Rank;

public interface RankRepository extends CrudRepository<Rank, String> {
}
