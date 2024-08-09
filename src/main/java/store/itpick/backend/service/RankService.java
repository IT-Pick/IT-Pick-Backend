package store.itpick.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import store.itpick.backend.dto.rank.RankResponseDTO;
import store.itpick.backend.model.CommunityPeriod;
import store.itpick.backend.model.Keyword;
import store.itpick.backend.model.Reference;
import store.itpick.backend.repository.CommunityPeriodRepository;
import store.itpick.backend.repository.KeywordRepository;

import java.util.List;
import java.util.Optional;


//관련자료 조회하는 Service
@Service
public class RankService {
    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private CommunityPeriodRepository communityPeriodRepository;

    public RankResponseDTO getReferenceByKeyword(String community, String period, String keyword) {
        List<String> communitiesToCheck;
        if ("total".equals(community)) {
            communitiesToCheck = List.of("naver", "zum", "nate");
        } else {
            communitiesToCheck = List.of(community);
        }

        for (String comm : communitiesToCheck) {
            // CommunityPeriod를 찾기
            Optional<CommunityPeriod> communityPeriodOptional = communityPeriodRepository.findByCommunityAndPeriod(comm, period);

            if (communityPeriodOptional.isPresent()) {
                CommunityPeriod communityPeriod = communityPeriodOptional.get();

                // CommunityPeriod에 연결된 키워드 찾기
                List<Keyword> keywords = keywordRepository.findByCommunityPeriods(communityPeriod);

                // 주어진 키워드가 있는지 확인
                Keyword keywordEntity = keywords.stream()
                        .filter(k -> k.getKeyword().equals(keyword))
                        .findFirst()
                        .orElse(null);

                if (keywordEntity != null) {
                    Reference reference = keywordEntity.getReference();

                    // DTO 생성
                    RankResponseDTO response = new RankResponseDTO();
                    response.setKeyword(keywordEntity.getKeyword());
                    response.setSearchLink(reference.getSearchLink());
                    response.setNewsTitle(reference.getNewsTitle());
                    response.setImageUrl(reference.getNewsImage());
                    response.setNewsContent(reference.getNewsContent());
                    response.setNewsLink(reference.getNewsLink());

                    return response;
                }
            }
        }

        // 키워드가 없거나 커뮤니티/기간이 없을 경우 null 반환 또는 예외 처리
        return null;
    }

}
