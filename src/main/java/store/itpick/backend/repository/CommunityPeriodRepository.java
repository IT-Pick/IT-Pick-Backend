package store.itpick.backend.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import store.itpick.backend.model.CommunityPeriod;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityPeriodRepository extends JpaRepository<CommunityPeriod, Long> {



    /** community가 'zum'이고 period가 'realtime'인 레코드 중에서 최근 업데이트된 10개를 조회하는 메서드 **/
    @Query("SELECT cp FROM CommunityPeriod cp WHERE cp.community = 'zum' AND cp.period = 'realtime' ORDER BY cp.communityPeriodId DESC")
    List<CommunityPeriod> findTop10ByZumAndRealtime(Pageable pageable);

    /** community가 'nate'이고 period가 'realtime'인 레코드 중에서 최근 업데이트된 10개를 조회하는 메서드 **/
    @Query("SELECT cp FROM CommunityPeriod cp WHERE cp.community = 'nate' AND cp.period = 'realtime' ORDER BY cp.communityPeriodId DESC")
    List<CommunityPeriod> findTop10ByNateAndRealtime(Pageable pageable);

    Optional<CommunityPeriod> findByCommunityAndPeriod(String community, String period);

}
