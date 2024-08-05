package store.itpick.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.itpick.backend.model.Keyword;

import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
<<<<<<< HEAD
    Optional<Keyword> findByRedisIdAndKeyword(String redisId, String keyword);
}
=======

    /** Key랑 RedisId를 통해서 관련자료 Id를 조회하는데, 2개 이상의 Id가 조회되는 경우 가장 최신의 것을 선택 **/
    Optional<Keyword> findFirstByRedisIdAndKeywordOrderByCreateAtDesc(String redisId, String keyword);
}
>>>>>>> parent of 982ff72 (feat : 실시간 검색할 시, 중복된 키워드 저장하면 덮어쓰게 저장하도록 함(reference는 현재 꺼로 업데이트))
