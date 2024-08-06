package store.itpick.backend.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import store.itpick.backend.model.Keyword;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    /** Key랑 RedisId를 통해서 관련자료 Id를 조회하는데, 2개 이상의 Id가 조회되는 경우 가장 최신의 것을 선택 **/
    Optional<Keyword> findFirstByRedisIdAndKeywordOrderByCreateAtDesc(String redisId, String keyword);

    /** Keyword저장할 때, 중복된 Keyword+RedisID 조합이 있는지 확인할 때 사용  **/
    Optional<Keyword> findFirstByRedisIdAndKeyword(String redisId, String keyword);


    /** redis_id가 'nate_by_real_time'인 레코드 중에서 최근 Nate 업데이트된 10개를 조회하는 메서드 **/
    @Query("SELECT k FROM Keyword k WHERE k.redisId = 'nate_by_real_time' ORDER BY k.updateAt DESC")
    List<Keyword> findTop10ByRedisIdForNate(Pageable pageable);

    /** redis_id가 'naver_by_real_time'인 레코드 중에서 최근 Naver 업데이트된 10개를 조회하는 메서드 **/
    @Query("SELECT k FROM Keyword k WHERE k.redisId = 'naver_by_real_time' ORDER BY k.updateAt DESC")
    List<Keyword> findTop10ByRedisIdForNaver(Pageable pageable);

    /** redis_id가 'zum_by_real_time'인 레코드 중에서 최근 Zum 업데이트된 10개를 조회하는 메서드 **/
    @Query("SELECT k FROM Keyword k WHERE k.redisId = 'zum_by_real_time' ORDER BY k.updateAt DESC")
    List<Keyword> findTop10ByRedisIdForZum(Pageable pageable);

}