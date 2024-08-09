package store.itpick.backend.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import store.itpick.backend.model.CommunityPeriod;
import store.itpick.backend.model.Keyword;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    Optional<Keyword> findByKeyword(String keyword);
    List<Keyword> findByCommunityPeriods(CommunityPeriod communityPeriod);


    @Query("SELECT k FROM Keyword k JOIN k.communityPeriods cp WHERE cp.community = 'naver' ORDER BY k.updateAt DESC")
    List<Keyword> findTop10ByCommunityNaver(Pageable pageable);

    @Query("SELECT k FROM Keyword k JOIN k.communityPeriods cp WHERE cp.community = 'nate' ORDER BY k.updateAt DESC")
    List<Keyword> findTop10ByCommunityNate(Pageable pageable);

    @Query("SELECT k FROM Keyword k JOIN k.communityPeriods cp WHERE cp.community = 'zum' ORDER BY k.updateAt DESC")
    List<Keyword> findTop10ByCommunityZum(Pageable pageable);

}