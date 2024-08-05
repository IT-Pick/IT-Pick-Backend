package store.itpick.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.itpick.backend.model.Keyword;
import store.itpick.backend.repository.KeywordRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    @Transactional
    public void saveAll(List<Keyword> keywords) {
        for (Keyword keyword : keywords) {
            Optional<Keyword> existingKeyword = keywordRepository.findFirstByRedisIdAndKeyword(keyword.getRedisId(), keyword.getKeyword());

            if (existingKeyword.isPresent()) {
                // 기존의 엔티티가 존재하면, 해당 엔티티의 필드 값을 업데이트
                Keyword keywordToUpdate = existingKeyword.get();
                keywordToUpdate.setStatus(keyword.getStatus());
                keywordToUpdate.setReference(keyword.getReference());
                keywordToUpdate.setUpdateAt(Timestamp.from(Instant.now())); // 업데이트 시간 변경
                // 필요한 경우 다른 필드도 업데이트

                keywordRepository.save(keywordToUpdate);
            } else {
                // 존재하지 않으면 새로운 엔티티를 저장
                keywordRepository.save(keyword);
            }
        }
    }

    @Transactional
    public void save(Keyword keyword) {
        keywordRepository.save(keyword);
    }
}
