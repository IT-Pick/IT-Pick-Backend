package store.itpick.backend.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import store.itpick.backend.model.CommunityPeriod;
import store.itpick.backend.model.Keyword;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    Optional<Keyword> findByKeyword(String keyword);



}