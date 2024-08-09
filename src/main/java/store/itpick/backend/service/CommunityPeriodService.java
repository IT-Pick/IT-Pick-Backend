package store.itpick.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import store.itpick.backend.model.CommunityPeriod;
import store.itpick.backend.repository.CommunityPeriodRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CommunityPeriodService {
    @Autowired
    private CommunityPeriodRepository communityPeriodRepository;

    public CommunityPeriod save(CommunityPeriod communityPeriod) {
        return communityPeriodRepository.save(communityPeriod);
    }



    public List<CommunityPeriod> saveAll(List<CommunityPeriod> communityPeriods) {
        return communityPeriodRepository.saveAll(communityPeriods);
    }

    public Optional<CommunityPeriod> findByCommunityAndPeriod(String community, String period) {
        return communityPeriodRepository.findByCommunityAndPeriod(community, period);
    }


}
