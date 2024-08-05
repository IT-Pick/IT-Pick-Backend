package store.itpick.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.itpick.backend.model.Keyword;

import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Optional<Keyword> findByRedisIdAndKeyword(String redisId, String keyword);
}
