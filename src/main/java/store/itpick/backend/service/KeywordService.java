package store.itpick.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.itpick.backend.model.Keyword;
import store.itpick.backend.repository.KeywordRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    @Transactional
    public void saveAll(List<Keyword> keywords) {
        keywordRepository.saveAll(keywords);
    }

    @Transactional
    public void save(Keyword keyword) {
        keywordRepository.save(keyword);
    }
}
