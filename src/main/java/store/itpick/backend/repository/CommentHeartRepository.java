package store.itpick.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import store.itpick.backend.model.CommentHeart;

@Repository
public interface CommentHeartRepository extends JpaRepository<CommentHeart,Long> {
}