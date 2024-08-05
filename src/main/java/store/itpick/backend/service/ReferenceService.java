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

    @Transactional
    public void saveAll(List<Reference> references) {
        referenceRepository.saveAll(references);
    }
}
