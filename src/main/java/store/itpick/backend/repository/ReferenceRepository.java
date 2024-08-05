package store.itpick.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.itpick.backend.model.Reference;

public interface ReferenceRepository extends JpaRepository<Reference, Long> {
}