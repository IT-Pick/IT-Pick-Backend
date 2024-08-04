package store.itpick.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import store.itpick.backend.dto.rank.RankResponseDTO;
import store.itpick.backend.model.Keyword;
import store.itpick.backend.model.Reference;
import store.itpick.backend.repository.KeywordRepository;

import java.util.Optional;


//관련자료 조회하는 Service
@Service
public class RankService {
    @Autowired
    private KeywordRepository keywordRepository;

    public RankResponseDTO getReferenceByKeyword(String key, String keyword) {
        Optional<Keyword> keywordEntityOptional = keywordRepository.findFirstByRedisIdAndKeywordOrderByCreateAtDesc(key, keyword);

        if (keywordEntityOptional.isPresent()) {
            Keyword keywordEntity = keywordEntityOptional.get();
            Reference reference = keywordEntity.getReference();

            RankResponseDTO response = new RankResponseDTO();
            response.setKeyword(keywordEntity.getKeyword());
            response.setSearchLink(reference.getSearchLink());
            response.setNewsTitle(reference.getNewsTitle());
            response.setImageUrl(reference.getNewsImage());
            response.setNewsContent(reference.getNewsContent());
            response.setNewsLink(reference.getNewsLink());

            return response;
        } else {
            // 키워드가 없는 경우 null 반환 또는 예외 처리
            return null;
        }
    }
}
