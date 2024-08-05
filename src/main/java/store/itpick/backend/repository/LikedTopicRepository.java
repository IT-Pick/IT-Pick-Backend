package store.itpick.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import store.itpick.backend.model.LikedTopic;

import java.util.List;

@Repository
public interface LikedTopicRepository extends JpaRepository<LikedTopic, Long> {
}
