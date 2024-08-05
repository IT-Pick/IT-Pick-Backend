package store.itpick.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.itpick.backend.model.Reference;
import store.itpick.backend.repository.ReferenceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReferenceService {

    private final ReferenceRepository referenceRepository;

    //관련자료들 List들을 저장함
    @Transactional
    public void saveAll(List<Reference> references) {
        referenceRepository.saveAll(references);
    }
}
